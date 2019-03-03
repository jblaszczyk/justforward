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
import just.forward.api.statement.Statement;

@Immutable
public final class ForAllProposition extends QuantifiedProposition {
	
	private ForAllProposition(Iterable<Variable> variables, Proposition proposition) {
		super(variables, proposition);
	}
	
	public static ForAllProposition newInstance(Iterable<Variable> variables, Proposition proposition) {
		return new ForAllProposition(variables, proposition);
	}

	public static ForAllProposition newInstance(Stream<Variable> variables, Proposition proposition) {
		return newInstance(variables.collect(Collectors.toSet()), proposition);
	}

	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return ForAllProposition.newInstance(
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
		
		return new ForAllProposition(ungroundVariables, groundChild);
	}
	
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return variables;
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return variables();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return getChild().getExistentiallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return getChild().existentiallyQuantifiedVariables();
	}

	@Override
	public ForAllProposition toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return ForAllProposition.newInstance(variables, getChild().toNNF());
	}

	@Override
	public ForAllProposition toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		Proposition newChild = getChild().toPNF();
		if( newChild instanceof ForAllProposition ) {
			ForAllProposition newForAllChild = (ForAllProposition) newChild;
			
			return ForAllProposition.newInstance(
				Stream.concat(variables(), newForAllChild.variables()), 
				newForAllChild.getChild()
			);
		}
		
		return ForAllProposition.newInstance(variables, newChild);
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
					.map(e -> ForAllProposition.newInstance( variables, e.toDNF() ))
			);
			
		} else {
			return ForAllProposition.newInstance(variables, getChild().toDNF()).toDNF();
		}		
	}

	@Override
	public ExistsProposition toNegated() {
		return ExistsProposition.newInstance(variables, getChild().toNegated());
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
		
		return ForAllProposition.newInstance(variables, newChild);
	}
	
	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterForAllProposition(this) ) {
			getChild().accept(visitor);
			visitor.leaveForAllProposition(this);
		}
	}

	@Override
	public String toString() {
		return "forall[" + Joiner.on(',').join(variables) + "](" + getChild().toString() +")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO ForAllStatement in StructuralComparator
		ForAllProposition other = (ForAllProposition) obj;
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
