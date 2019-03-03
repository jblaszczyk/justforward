package just.forward.normalization.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.Parameters;
import just.forward.api.common.Predicate;
import just.forward.api.common.QualifiedName;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;
import just.forward.api.model.Operator;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Literal;
import just.forward.normalization.model.NormalizedRule.Builder;

/*
	All operator preconditions are conjunctions of literals.
 */
@Immutable
public class NormalizedOperator {
	
	private final boolean isSynthetic; // TODO needed?
	private final Operator originalOperator;
//	private final ImmutableList<Variable> parameters;
	private final Parameters parameters;
	private final ImmutableSet<Literal> preconditions;
	private final ImmutableSet<NormalizedEffect> effects;
	
	@Nullable 
	private final QualifiedName name;
	
	NormalizedOperator(boolean isSynthetic, @Nullable Operator originalOperator, @Nullable QualifiedName name, Parameters parameters, Iterable<Literal> preconditions, Iterable<NormalizedEffect> effects) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(preconditions);
		Objects.requireNonNull(effects);
		Objects.requireNonNull(parameters);
		this.isSynthetic = isSynthetic;
		this.name = name;
		this.originalOperator = originalOperator;
//		this.parameters = ImmutableList.copyOf(parameters);
		this.parameters = parameters;
		this.preconditions = ImmutableSet.copyOf(preconditions);
		this.effects = ImmutableSet.copyOf(effects);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public Stream<Literal> preconditions() {
		return preconditions.stream();
	}
	
	public Stream<NormalizedEffect> effects() {
		return effects.stream();
		
	}
	
	public GroundNormalizedAction ground(VariableSubstitution substitution) {
		return GroundNormalizedAction.newInstance(this, substitution);
	}
	
	Stream<Predicate> predicatesInPreonditions() {
		Stream<Predicate> pre = preconditions()
			.filter( e -> e instanceof AtomLiteral)
			.map( e -> ((AtomLiteral) e).getPredicate() );
				
		Stream<Predicate> when = effects()
			.flatMap( e -> e.predicatesInConditions() );

		return Stream.concat( pre, when );
	}

	Stream<Predicate> predicatesInEffects() {
		return effects()
			.flatMap( e -> e.predicatesInEffects() );
	}
	
	public boolean isSynthetic() {
		return isSynthetic;
	}
	
	public Operator getOriginal() {
		return originalOperator;
	}
	
	public QualifiedName getName() {
		return name;
	}

	public Stream<Variable> parameters() {
		return parameters.variables();
	}
	
	public Set<Variable> getAllVariables() {
		return allVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> allVariables() {
		return Stream.concat(freeVariables(), quantifiedVariables());
	}
	public Set<Variable> getQuantifiedVariables() {
		return quantifiedVariables().collect(Collectors.toSet());
	}	
	public Stream<Variable> quantifiedVariables() {
		return Stream.concat(universallyQuantifiedVariables(), existentiallyQuantifiedVariables());
	}
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> freeVariables() {
		return Stream.concat(
			preconditions()
				.flatMap(Literal::freeVariables),
			effects()
				.flatMap(NormalizedEffect::freeVariables)
		);
	}
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> universallyQuantifiedVariables() {
		return Stream.concat(
			preconditions()
				.flatMap(Literal::universallyQuantifiedVariables), // should be empty
			effects()
				.flatMap(NormalizedEffect::universallyQuantifiedVariables)
		);
	}
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return Stream.concat(
			preconditions()
				.flatMap(Literal::existentiallyQuantifiedVariables), // should be empty
			effects()
				.flatMap(NormalizedEffect::existentiallyQuantifiedVariables) // should be empty
		);
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
				parameters,
				preconditions,
				effects
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		NormalizedOperator other = (NormalizedOperator) obj;
		return Objects.equals(this.parameters, other.parameters) &&
				Objects.equals(this.preconditions, other.preconditions) &&
				Objects.equals(this.effects, other.effects);
	}
	
	@Override
	public String toString() {
		return name.toString() + parameters.toString();
	}
	
	public static class Builder {

		Operator originalOperator;
		List<Variable> parameters = Lists.newArrayList();
		List<Literal> preconditions = Lists.newArrayList();
		List<NormalizedEffect> effects = Lists.newArrayList();
		QualifiedName name;
		boolean isSynthetic;
		
		Builder() {}
		
		public Builder synthetic() {
			isSynthetic = true;
			return this;
		}
		
		public Builder synthetic(boolean isSynthetic) {
			this.isSynthetic = isSynthetic;
			return this;
		}
		
		public Builder original(Operator original) {
			Objects.requireNonNull(original);
			this.originalOperator = original;
			return this;
		}

		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}
		
		public Builder parameter(Variable parameter) {
			Objects.requireNonNull(parameter);
			parameters.add(parameter);
			return this;
		}
		
		public Builder parameters(Iterable<Variable> parameters) {
			Objects.requireNonNull(parameters);
			Iterables.addAll( this.parameters, parameters );
			return this;
		}
		
		public Builder parameters(Stream<Variable> parameters) {
			Objects.requireNonNull(parameters);
			this.parameters.addAll( parameters.collect( Collectors.toList() ));
			return this;
		}

		public Builder parameters(Variable... parameters) {
			Objects.requireNonNull(parameters);
			Iterables.addAll( this.parameters, Arrays.asList(parameters) );
			return this;
		}		
		
		public Builder parameters(Parameters parameters) {
			Objects.requireNonNull(parameters);
			this.parameters.addAll( parameters.getVariables() );
			return this;
		}

		public Builder precondition(Literal precondition) {
			Objects.requireNonNull(precondition);
			preconditions.add(precondition);
			return this;
		}
		
		public Builder preconditions(Iterable<Literal> preconditions) {
			Objects.requireNonNull(preconditions);
			Iterables.addAll( this.preconditions, preconditions );
			return this;
		}
		
		public Builder preconditions(Stream<Literal> preconditions) {
			Objects.requireNonNull(preconditions);
			this.preconditions.addAll( preconditions.collect( Collectors.toList() ));
			return this;
		}

		public Builder preconditions(Literal... preconditions) {
			Objects.requireNonNull(preconditions);
			Iterables.addAll( this.preconditions, Arrays.asList(preconditions) );
			return this;
		}

		public Builder effect(NormalizedEffect effect) {
			Objects.requireNonNull(effect);
			effects.add(effect);
			return this;
		}
		
		public Builder effects(Iterable<NormalizedEffect> effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, effects );
			return this;
		}
		
		public Builder effects(Stream<NormalizedEffect> effects) {
			Objects.requireNonNull(effects);
			this.effects.addAll( effects.collect( Collectors.toList() ));
			return this;
		}

		public Builder effects(NormalizedEffect... effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, Arrays.asList(effects) );
			return this;
		}
		
		public NormalizedOperator build() {
			return new NormalizedOperator(isSynthetic, originalOperator, name, Parameters.newInstance(parameters), preconditions, effects);
		}
	}
	
	public Set<Literal> getPreconditions() {
		return preconditions;
	}

	public Set<NormalizedEffect> getEffects() {
		return effects;
	}

	public List<Variable> getParameters() {
		return parameters.getVariables();
	}
	
	public Parameters getParametersStuct() {
		return parameters;
	}
}
