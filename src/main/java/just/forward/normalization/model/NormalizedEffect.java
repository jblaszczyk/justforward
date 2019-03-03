package just.forward.normalization.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import just.forward.api.common.Predicate;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Literal;

/*
	All effect conditions are conjunctions of literals.
	All operator effects are conjunctions of universally quantified conditional simple effects
 */
@Immutable
public class NormalizedEffect {

	ImmutableSet<Variable> universallyQuantifiedVars; // maybe this should be in NoralizedOperator?
	ImmutableSet<Literal> conditions;
	ImmutableSet<Literal> effects;
	// TODO for fluents assignments separate set of FluentAssignment
	
	NormalizedEffect(Iterable<Variable> universallyQuantifiedVariables, Iterable<Literal> conditions, Iterable<Literal> effects) {
		Objects.requireNonNull(universallyQuantifiedVariables);
		Objects.requireNonNull(conditions);
		Objects.requireNonNull(effects);
		this.universallyQuantifiedVars = ImmutableSet.copyOf(universallyQuantifiedVariables);
		this.conditions = ImmutableSet.copyOf(conditions);
		this.effects = ImmutableSet.copyOf(effects);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public boolean isUniversallyQuantified() {
		return !universallyQuantifiedVars.isEmpty();
	}
	
	public boolean isConditional() {
		return !conditions.isEmpty();
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
			conditions()
				.flatMap(Literal::freeVariables),
			effects()
				.flatMap(Literal::freeVariables)
		);
	}
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> universallyQuantifiedVariables() {
		return universallyQuantifiedVars.stream();
	}
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return Stream.concat(
			conditions()
				.flatMap(Literal::existentiallyQuantifiedVariables), // should be empty
			effects()
				.flatMap(Literal::existentiallyQuantifiedVariables) // should be empty
		);
	}	
	
	public Set<Literal> getConditions() {
		return ImmutableSet.copyOf(conditions);
	}

	public Stream<Literal> conditions() {
		return conditions.stream();
	}
	
	public Stream<Literal> effects() {
		return effects.stream();
	}
	
	public boolean isGround() {
		return universallyQuantifiedVars.isEmpty() &&
			conditions().allMatch(Literal::isGround) &&
			effects().allMatch(Literal::isGround);
	}
	
	public NormalizedEffect ground(VariableSubstitution substitution) {
		Set<Variable> newUniversallyQuantifiedVariables = Sets.newHashSet(universallyQuantifiedVars);
		newUniversallyQuantifiedVariables.removeAll(substitution.keySet());
		
		Set<Literal> newConditions = conditions()
			.map(e -> e.ground(substitution))
			.collect(Collectors.toSet());
		
		Set<Literal> newEffects = effects()
			.map(e -> e.ground(substitution))
			.collect(Collectors.toSet());
		
		return new NormalizedEffect(newUniversallyQuantifiedVariables, newConditions, newEffects);
	}
	
	Stream<Predicate> predicatesInConditions() {
		return conditions()
		.filter( e -> e instanceof AtomLiteral)
		.map(e -> ((AtomLiteral) e).getPredicate() );
	}
	
	Stream<Predicate> predicatesInEffects() {
		return effects()
		.filter( e -> e instanceof AtomLiteral)
		.map(e -> ((AtomLiteral) e).getPredicate() );
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				universallyQuantifiedVars,
				conditions,
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
		
		NormalizedEffect other = (NormalizedEffect) obj;
		return Objects.equals(this.universallyQuantifiedVars, other.universallyQuantifiedVars) &&
				Objects.equals(this.conditions, other.conditions) &&
				Objects.equals(this.effects, other.effects);
	}	

	public static class Builder {
		List<Variable> variables = Lists.newArrayList();
		List<Literal> conditions = Lists.newArrayList();
		List<Literal> effects = Lists.newArrayList();

		Builder() {}
		
		public Builder condition(Literal condition) {
			Objects.requireNonNull(condition);
			conditions.add(condition);
			return this;
		}
		
		public Builder conditions(Iterable<Literal> conditions) {
			Objects.requireNonNull(conditions);
			Iterables.addAll( this.conditions, conditions );
			return this;
		}
		
		public Builder conditions(Stream<Literal> conditions) {
			Objects.requireNonNull(conditions);
			this.conditions.addAll( conditions.collect( Collectors.toList() ));
			return this;
		}

		public Builder conditions(Literal... conditions) {
			Objects.requireNonNull(conditions);
			Iterables.addAll( this.conditions, Arrays.asList(conditions) );
			return this;
		}
		
		public Builder effect(Literal effect) {
			Objects.requireNonNull(effect);
			effects.add(effect);
			return this;
		}
		
		public Builder effects(Iterable<Literal> effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, effects );
			return this;
		}
		
		public Builder effects(Stream<Literal> effects) {
			Objects.requireNonNull(effects);
			this.effects.addAll( effects.collect( Collectors.toList() ));
			return this;
		}

		public Builder effects(Literal... effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, Arrays.asList(effects) );
			return this;
		}
		
		public Builder variable(Variable variable) {
			Objects.requireNonNull(variable);
			variables.add(variable);
			return this;
		}
		
		public Builder variables(Iterable<Variable> variable) {
			Objects.requireNonNull(variable);
			Iterables.addAll( this.variables, variable );
			return this;
		}
		
		public Builder variables(Stream<Variable> variable) {
			Objects.requireNonNull(variable);
			this.variables.addAll( variable.collect( Collectors.toList() ));
			return this;
		}

		public Builder variables(Variable... variable) {
			Objects.requireNonNull(variable);
			Iterables.addAll( this.variables, Arrays.asList(variable) );
			return this;
		}		
		public NormalizedEffect build() {
			return new NormalizedEffect(variables, conditions, effects);
		}
	}

	public Set<Literal> getEffects() {
		return effects;
	}

	@Override
	public String toString() {
		String result = "";
		if( !universallyQuantifiedVars.isEmpty() ) {
			result += "forall(" + Joiner.on(",").join(universallyQuantifiedVars) + ") ";
		}
		
		result += "when(" + Joiner.on(',').join(conditions) + ") ";
		result += "then(" + Joiner.on(',').join(effects) + ")";

		return result;
	}
}
