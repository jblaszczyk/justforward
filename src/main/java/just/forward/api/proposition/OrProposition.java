package just.forward.api.proposition;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.statement.QuantifiedStatement;

@Immutable
public final class OrProposition extends AbstractCompoundProposition {
	
	private OrProposition(Iterable<? extends Proposition> children) {
		super(children);
	}
	
	public static OrProposition newInstance(Iterable<? extends Proposition> children) {
		return newInstance(ImmutableList.copyOf(children).stream());
	}
	
	public static OrProposition newInstance(Proposition... children) {
		return newInstance(Stream.of(children));
	}

	public static OrProposition newInstance(Stream<? extends Proposition> children) {
		List<? extends Proposition> op = children.collect(Collectors.toList());
		if( op.isEmpty() ) {
			throw new IllegalArgumentException("AndProposition must have at least one operand");
		}
		
		return new OrProposition(op);
	}
	
	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return OrProposition.newInstance(
			children().map(e -> e.substitute(substitution))
		);
	}
	
	public OrProposition merge(OrProposition other) {
		if( other.isEmpty() ) {
			return this;
		}
		
		List<Proposition> args = Lists.newArrayList(getChildren());
		args.addAll(other.getChildren());
		
		return OrProposition.newInstance(args);
	}
	
	@Override
	public OrProposition ground(VariableSubstitution subsitution) {
		return OrProposition.newInstance(groundChildren(subsitution));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}	
	@Override
	public Stream<Variable> freeVariables() {
		return children().flatMap(Proposition::freeVariables);
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return children().flatMap(Proposition::universallyQuantifiedVariables);
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return children().flatMap(Proposition::existentiallyQuantifiedVariables);
	}

	@Override
	public boolean isDNF() {
		if( children().anyMatch(e -> e.uses(OrProposition.class) )) {
			return false;
		}
			
		return children().allMatch(Proposition::isDNF);
	}
	
	@Override
	public boolean isPNF() {
		return !contains(QuantifiedProposition.class);
	}

	@Override
	public OrProposition toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return OrProposition.newInstance(
				children().map(Proposition::toNNF)
		);
	}
	
	@Override
	public OrProposition removeQuantifiers() {
		if( !contains(QuantifiedProposition.class) ) {
			return this;
		}
		
		return OrProposition.newInstance(
			children().map(Proposition::removeQuantifiers)
		);
	}

	@Override
	public AndProposition toNegated() {
		return AndProposition.newInstance(
				children().map(Proposition::toNegated)
		);
	}

	@Override
	public Proposition toSimplified() {
		List<Proposition> newChildren = children()
			.map(Proposition::toSimplified)
			.filter(EmptyProposition.filterOut())
			.collect(Collectors.toList());

		List<Proposition> newChildrenOr = newChildren.stream()
			.filter(e -> e instanceof OrProposition)
			.map(e -> ((OrProposition) e))
			.flatMap( e -> e.children())
			.collect(Collectors.toList());
				
		List<Proposition> newChildrenRest = newChildren.stream()
			.filter(e -> !(e instanceof OrProposition))
			.collect(Collectors.toList());
			
		newChildren = Stream.concat(newChildrenRest.stream(), newChildrenOr.stream())
				.filter(EmptyProposition.filterOut())
				.collect(Collectors.toList());

		if( newChildren.isEmpty() ) {
			return EmptyProposition.getInstance();
		}
		
		if( newChildren.contains(BooleanLiteral.getTrue()) ) {
			return BooleanLiteral.getTrue();
		}
		
		if( newChildren.stream().allMatch(BooleanLiteral.filterFalse()) ) {
			return BooleanLiteral.getFalse();
		}

		newChildren = newChildren.stream()
				.filter(BooleanLiteral.filterOutFalse())
				.collect(Collectors.toList());
		
		if( newChildren.size() == 1 ) {
			return newChildren.get(0);
		}

		if( directlyComplementsEachOther(newChildren) ) {
			return BooleanLiteral.getTrue();
		}
		
		return OrProposition.newInstance(newChildren);
	}
	
	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterOrProposition(this) ) {
			children.forEach(e -> e.accept(visitor));
			visitor.leaveOrProposition(this);
		}
	}

	@Override
	public String toString() {
		return "or(" + Joiner.on(',').join(children) + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		OrProposition other = (OrProposition) obj;
		return Objects.equals(this.getChildren(), other.getChildren());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
			getClass().getName(),
			getChildren()
		);
	}

	@Override
	public OrProposition toDNF() {
		if( isDNF() ) {
			return this;
		}
		
		List<Proposition> processedChildren = children().map(Proposition::toDNF).collect(Collectors.toList());
		
		List<Proposition> args = Lists.newArrayList();
		for(Proposition processedChild : processedChildren) {
			
			if( processedChild instanceof OrProposition ) {
				args.addAll( Lists.newArrayList( ((OrProposition)processedChild).getChildren() ) );
				
			} else {
				args.add( processedChild );
			}
		}
		
		return OrProposition.newInstance(args);
	}
}
