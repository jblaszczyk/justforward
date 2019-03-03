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
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.TermVisitor;
import just.forward.api.expression.Variable;
import just.forward.api.statement.AndStatement;
import just.forward.api.statement.QuantifiedStatement;
import just.forward.api.statement.Statement;

@Immutable
public final class AndProposition extends AbstractCompoundProposition {
	
	private AndProposition(Iterable<? extends Proposition> children) {
		super(children);
	}
	
	public static AndProposition newInstance(Iterable<? extends Proposition> children) {
		return newInstance(ImmutableList.copyOf(children).stream());
	}
	
	public static AndProposition newInstance(Proposition... children) {
		return newInstance(Stream.of(children));
	}

	public static AndProposition newInstance(Stream<? extends Proposition> children) {
		List<? extends Proposition> op = children.collect(Collectors.toList());
		if( op.isEmpty() ) {
			throw new IllegalArgumentException("AndProposition must have at least one operand");
		}
		
		return new AndProposition(op);
	}

	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return AndProposition.newInstance(
			children().map(e -> e.substitute(substitution))
		);
	}
	
	public AndProposition merge(AndProposition other) {
		if( other.isEmpty() ) {
			return this;
		}
		
		List<Proposition> args = Lists.newArrayList(getChildren());
		args.addAll(other.getChildren());
		
		return AndProposition.newInstance(args);
	}
	
	@Override
	public AndProposition ground(VariableSubstitution subsitution) {
		return AndProposition.newInstance(groundChildren(subsitution));
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
		if( children().anyMatch(e -> e.uses(AndProposition.class)) ) {
			return false;
		}

		if( children().anyMatch(e -> e.uses(OrProposition.class)) ) {
			return false;
		}
		
		return children().allMatch(Proposition::isDNF);
	}
	
	@Override
	public boolean isPNF() {
		return !contains(QuantifiedProposition.class);
	}

	@Override
	public AndProposition toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return new AndProposition(children().map(Proposition::toNNF).collect(Collectors.toList()));
	}

	@Override
	public AndProposition removeQuantifiers() {
		if( !contains(QuantifiedProposition.class) ) {
			return this;
		}
		
		return AndProposition.newInstance(
			children().map(Proposition::removeQuantifiers)
		);
	}

	@Override
	public OrProposition toNegated() {
		return OrProposition.newInstance(
			children().map(Proposition::toNegated)
		);
	}
	
	@Override
	public Proposition toSimplified() {
		List<Proposition> newChildren = children()
			.map(Proposition::toSimplified)
			.filter(EmptyProposition.filterOut())
			.collect(Collectors.toList());

		List<Proposition> newChildrenAnd = newChildren.stream()
			.filter(e -> e instanceof AndProposition)
			.map(e -> ((AndProposition) e))
			.flatMap( e -> e.children())
			.collect(Collectors.toList());
			
		List<Proposition> newChildrenRest = newChildren.stream()
			.filter(e -> !(e instanceof AndProposition))
			.collect(Collectors.toList());
		
		newChildren = Stream.concat(newChildrenRest.stream(), newChildrenAnd.stream())
			.filter(EmptyProposition.filterOut())
			.collect(Collectors.toList());
			
		if( newChildren.isEmpty() ) {
			return EmptyProposition.getInstance();
		}

		if( newChildren.contains(BooleanLiteral.getFalse()) ) {
			return BooleanLiteral.getFalse();
		}
		
		if( newChildren.stream().allMatch(BooleanLiteral.filterTrue()) ) {
			return BooleanLiteral.getTrue();
		}

		newChildren = newChildren.stream()
				.filter(BooleanLiteral.filterOutTrue())
				.collect(Collectors.toList());
		
		if( newChildren.size() == 1 ) {
			return newChildren.get(0);
		}
		
		if( directlyComplementsEachOther(newChildren) ) {
			return BooleanLiteral.getFalse();
		}
		
		return AndProposition.newInstance(newChildren);
	}
	
	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterAndProposition(this) ) {
			children.forEach(e -> e.accept(visitor));
			visitor.leaveAndProposition(this);
		}
	}

	@Override
	public String toString() {
		return "and(" + Joiner.on(',').join(children) + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO AndStatement in StructuralComparator
		AndProposition other = (AndProposition) obj;
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
	public Proposition toDNF() {
		if( isDNF() ) {
			return this;
		}
		
		List<Proposition> processedChildren = children().map(Proposition::toDNF).collect(Collectors.toList());
		List<List<Proposition>> processedOrList = Lists.newArrayList();
		List<Proposition> processedHeadList = Lists.newArrayList();
		
		for(Proposition processedChild : processedChildren) {
			
			if( processedChild instanceof OrProposition ) {
				processedOrList.add( Lists.newArrayList( ((OrProposition)processedChild).getChildren() ) );
				
			} else if( processedChild instanceof AndProposition ) {
				processedHeadList.addAll( Lists.newArrayList( ((AndProposition)processedChild).getChildren() ) );
				
			} else {
				processedHeadList.add( processedChild );
			}
		}
		
		List<List<Proposition>> productOrListMergedWithHead = Lists.newArrayList();
		
		if( processedOrList.isEmpty() ) {
//			return sentence;
			productOrListMergedWithHead.add(processedHeadList);
			
		} else {
			List<List<Proposition>> productOrList = Lists.cartesianProduct(processedOrList);

			for( List<Proposition> list : productOrList ) {
				List<Proposition> sentences = Lists.newArrayList(processedHeadList);
				for( Proposition s : list ) {
					if( s instanceof AndProposition ) {
						sentences.addAll(((AndProposition) s).getChildren());
					} else {
						sentences.add(s);
					}
				}
				productOrListMergedWithHead.add(sentences);
			}
		}
		
		List<Proposition> arg = Lists.newArrayList();
		for( List<Proposition> inner : productOrListMergedWithHead ) {
			if( inner.size() > 1 ) {
				arg.add(AndProposition.newInstance(inner));
			} else {
				arg.add(inner.get(0));
			}

		}
		
		if( arg.size() > 1 ) {
			return OrProposition.newInstance(arg.stream().map(Proposition::toDNF));
//			return new OrProposition(arg);
		} else {
			return arg.get(0);
		}
	}

}
