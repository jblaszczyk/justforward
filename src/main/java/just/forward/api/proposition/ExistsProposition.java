package just.forward.api.proposition;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;

@Immutable
public final class ExistsProposition extends QuantifiedProposition {
	
	private ExistsProposition(Iterable<Variable> variables, Proposition proposition) {
		super(variables, proposition);
	}
	
	public static ExistsProposition newInstance(Iterable<Variable> variables, Proposition proposition) {
		return new ExistsProposition(variables, proposition);
	}
	
	public static ExistsProposition newInstance(Stream<Variable> variables, Proposition proposition) {
		return newInstance(variables.collect(Collectors.toSet()), proposition);
	}

	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return ExistsProposition.newInstance(
			variables()
				.map(e -> e.substitute(substitution))
				.filter(e -> e instanceof Variable)
				.map(e -> (Variable) e), 
			getChild().substitute(substitution)
		);
	}

	@Override
	public Proposition ground(VariableSubstitution substitution) {
		Proposition groundChild = getChild().ground(substitution);
		
		if( substitution.containsAll(variables) ) {
			return groundChild;
		}
		
		Set<Variable> ungroundVariables = Sets.newHashSet(variables);
		ungroundVariables.removeAll(substitution.keySet());
		
		return new ExistsProposition(ungroundVariables, groundChild);
	}
	
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return getChild().getUniversallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return getChild().universallyQuantifiedVariables();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return variables;
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return variables();
	}

	@Override
	public ExistsProposition toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return ExistsProposition.newInstance(variables, getChild().toNNF());
	}

	@Override
	public ExistsProposition toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		Proposition newChild = getChild().toPNF();
		if( newChild instanceof ExistsProposition ) {
			ExistsProposition newForAllChild = (ExistsProposition) newChild;
			
			return ExistsProposition.newInstance(
				Stream.concat(variables(), newForAllChild.variables()), 
				newForAllChild.getChild()
			);
		}
		
		return ExistsProposition.newInstance(variables, newChild);
	}
	
	@Override
	public Proposition toDNF() {
		if( isDNF() ) {
			return this;
		}
		
		if( getChild() instanceof OrProposition ) {
			OrProposition or = (OrProposition) getChild();
			
			return OrProposition.newInstance(
				or.children()
					.map(e -> ExistsProposition.newInstance( variables, e.toDNF() ))
			);
			
		} else {
			return ExistsProposition.newInstance(variables, getChild().toDNF()).toDNF();
		}
	}

	@Override
	public Proposition toNegated() {
		return ForAllProposition.newInstance(variables, getChild().toNegated());
	}
	
	@Override
	public Proposition toSimplified() {
		Proposition newChild = getChild().toSimplified();
		if( newChild.isEmpty() ) {
			return EmptyProposition.getInstance();
		}
		
		if( variables.isEmpty() ) {
			return newChild;
		}
		
		return ExistsProposition.newInstance(variables, newChild);
	}
	
	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterExistsProposition(this) ) {
			getChild().accept(visitor);
			visitor.leaveExistsProposition(this);
		}
	}

	@Override
	public String toString() {
		return "exists[" + Joiner.on(',').join(variables) + "](" + getChild().toString() +")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		ExistsProposition other = (ExistsProposition) obj;
		return Objects.equals(this.getChildren(), other.getChildren());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			getClass().getName(),
			getChildren()
		);
	}	
}
