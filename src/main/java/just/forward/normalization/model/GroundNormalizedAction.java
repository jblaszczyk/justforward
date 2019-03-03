package just.forward.normalization.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Term;
import just.forward.api.expression.Tuple;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.Literal;
import just.forward.util.ImmutableCollectors;

@Immutable
public class GroundNormalizedAction {

	private final NormalizedOperator operator;
	private final Tuple arguments;
	private final VariableSubstitution substitution;
	private final ImmutableSet<Literal> preconditions;
	private final ImmutableSet<NormalizedEffect> effects;

	private GroundNormalizedAction(NormalizedOperator operator, VariableSubstitution substitution) {
		Objects.requireNonNull(operator);
		Objects.requireNonNull(substitution);
		
		if( !substitution.keySet().containsAll(operator.getParameters()) ) {
			throw new IllegalArgumentException("Some arguments of GroundNormalizedAction are not ground");
		}
		
		Set<Variable> quantifiedVariablesToGround = operator.effects()
			.flatMap(NormalizedEffect::universallyQuantifiedVariables)
			.collect(Collectors.toSet());
		
		if( !substitution.keySet().containsAll(quantifiedVariablesToGround) ) {
			throw new IllegalArgumentException("Some universally quantified variables of GroundNormalizedAction are not ground");
		}
		
		this.operator = operator;
		this.arguments = operator.getParametersStuct().ground(substitution);
		this.preconditions = operator.preconditions()
			.map(e -> e.ground(substitution))
			.collect(ImmutableCollectors.toSet());
		this.effects = operator.effects()
				.map(e -> e.ground(substitution))
				.collect(ImmutableCollectors.toSet());
		this.substitution = substitution;
	}
	
	public static GroundNormalizedAction newInstance(NormalizedOperator operator, VariableSubstitution substitution) {
		return new GroundNormalizedAction(operator, substitution);
	}
	
	public NormalizedOperator getOperator() {
		return operator;
	}
	
	public QualifiedName getName() {
		return operator.getName();
	}
	
	public Tuple getArgumentsTuple() {
		return arguments;
	}
	
	public List<Term> getArguments() {
		return getArgumentsTuple().getTerms();
	}
	
	public Stream<Term> arguments() {
		return getArguments().stream();
	}
	
	public Set<Literal> getPreconditions() {
		return preconditions;
	}
	
	public Stream<Literal> preconditions() {
		return preconditions.stream();
	}

	public Set<NormalizedEffect> getEffects() {
		return effects;
	}

	public Stream<NormalizedEffect> effects() {
		return effects.stream();
	}
	
	public VariableSubstitution getSubstitution() {
		return substitution;
	}
	
	@Override
	public String toString() {
		return operator.getName().toString() + getArgumentsTuple().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		GroundNormalizedAction other = (GroundNormalizedAction) obj;
		return Objects.equals(this.operator, other.operator) &&
				Objects.equals(this.substitution, other.substitution);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass(),
				operator,
				substitution
		);
	}
}
