package just.forward.normalization.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import just.forward.api.common.Predicate;
import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Constant;
import just.forward.api.model.Domain;
import just.forward.util.ImmutableCollectors;

@Immutable
public class NormalizedDomain {

	Domain originalDomain;
	ImmutableSet<NormalizedOperator> operators;
	ImmutableSet<Constant> constants;
	ImmutableSet<Predicate> allPredicates;
	ImmutableSet<Predicate> mutablePredicates;
	ImmutableSet<Predicate> immutablePredicates;
	ImmutableSet<Predicate> derivedPredicates;
	ImmutableSet<NormalizedRule> rules;
	ImmutableSetMultimap<Predicate, NormalizedRule> predicatesToRulesMap;
	
	@Nullable
	QualifiedName name;
	
	NormalizedDomain(@Nullable QualifiedName name, @Nullable Domain originalDomain, Iterable<Constant> constants, Iterable<NormalizedOperator> operators, Iterable<NormalizedRule> rules) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(constants);
		Objects.requireNonNull(operators);
		Objects.requireNonNull(rules);
		this.originalDomain = originalDomain;
		this.operators = ImmutableSet.copyOf( operators );
		this.constants = ImmutableSet.copyOf( constants );
//		this.rules = ImmutableSet.copyOf( rules );
		this.name = name;
		
		buildPredicatesAndRules(Sets.newHashSet(rules));
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public Domain getOriginal() {
		return originalDomain;
	}
	
	// TODO test
	private void buildPredicatesAndRules(Set<NormalizedRule> rules) {
		Set<Predicate> predicatesInPreconditions = Sets.newHashSet();
		Set<Predicate> simplePredicatesInEffects = Sets.newHashSet();

		SetMultimap<Predicate, NormalizedRule> predicatesToRulesMap = rules.stream()
				.collect(ImmutableCollectors.toSetMultimap(e -> e.getPredicate(), e -> e));
//		SetMultimap<Predicate, NormalizedRule> predicatesToRulesMap = SetMultimapBuilder.hashKeys().hashSetValues().build();
//		rules.forEach( rule -> {
//			predicatesToRulesMap.put(rule.getPredicate(), rule);
//		});
		
		operators.stream()
			.forEach( e -> processOperator(predicatesToRulesMap, e, predicatesInPreconditions, simplePredicatesInEffects));

		Set<Predicate> simplePredicatesInPreconditions = predicatesInPreconditions.stream()
			.filter( predicate -> !predicatesToRulesMap.containsKey(predicate) )
			.collect(Collectors.toSet());
		
		Set<Predicate> derivedPredicatesInPreconditions = predicatesInPreconditions.stream()
				.filter( predicate -> predicatesToRulesMap.containsKey(predicate) )
				.collect(Collectors.toSet());
		
		// == derivedPredicatesInPreconditions ==
		this.derivedPredicates = ImmutableSet.copyOf( derivedPredicatesInPreconditions );
		
		// == rules ==
		this.rules = rules.stream()
			.filter( rule -> derivedPredicatesInPreconditions.contains(rule.getPredicate()))
			.collect( ImmutableCollectors.toSet() );

		// == predicatesToRulesMap ==
		this.predicatesToRulesMap = this.rules.stream()
			.collect(ImmutableCollectors.toSetMultimap(e -> e.getPredicate(), e -> e));
//		SetMultimap<Predicate, NormalizedRule> predicatesToRulesMap2 = SetMultimapBuilder.hashKeys().hashSetValues().build();
//		this.rules.forEach( rule -> {
//			predicatesToRulesMap2.put(rule.getPredicate(), rule);
//		});
//		this.predicatesToRulesMap = ImmutableSetMultimap.copyOf(predicatesToRulesMap2);
			
		// == mutablePredicates ==
		this.mutablePredicates = ImmutableSet.copyOf( simplePredicatesInEffects );

		// == immutablePredicates ==
		this.immutablePredicates = simplePredicatesInPreconditions.stream()
			.filter( e -> !mutablePredicates.contains(e) )
			.collect(ImmutableCollectors.toSet());
	}
	
	private void processOperator(
		SetMultimap<Predicate, NormalizedRule> predicatesToRulesMap, 
		NormalizedOperator operator,
		Set<Predicate> predicatesInPreconditions,		
		Set<Predicate> predicatesInEffects
	) {
		
		predicatesInEffects.addAll( operator.predicatesInEffects().collect(Collectors.toSet()) );
//		predicatesInEffects.addAll( operator.effects()
//			.flatMap( eff -> eff.conditions() )
//			.map( atom -> atom.getOriginal() )
//			.filter( e -> e instanceof Atom)
//			.map(e -> ((Atom) e).getPredicate() )
//			.collect(Collectors.toSet()) );
		
		predicatesInPreconditions.addAll( operator.predicatesInPreonditions().collect(Collectors.toSet()) );
//		predicatesInPreconditions.addAll( operator.preconditions()
//			.map( atom -> atom.getOriginal() )
//			.filter( e -> e instanceof Atom)
//			.map(e -> ((Atom) e).getPredicate() )
//			.collect(Collectors.toSet()) );
		
		operator.predicatesInPreonditions()
			.flatMap( predicate -> predicatesToRulesMap.get(predicate).stream() )
			.forEach( rule -> processRule( predicatesToRulesMap, rule, predicatesInPreconditions ) );
//		operator.preconditions()
//			.map( atom -> atom.getOriginal() )
//			.filter( e -> e instanceof Atom)
//			.map(e -> ((Atom) e).getPredicate() )
//			.flatMap( predicate -> predicatesToRulesMap.get(predicate).stream() )
//			.forEach( rule -> processRule( predicatesToRulesMap, rule, predicatesInPreconditions ) );
	}

	private void processRule(
		SetMultimap<Predicate, NormalizedRule> predicatesToRulesMap, 
		NormalizedRule rule,
		Set<Predicate> predicatesInPreconditions		
	) {
		predicatesInPreconditions.addAll( rule.predicatesInBody()
				.collect(Collectors.toSet()) );
//		predicatesInPreconditions.addAll( rule.body()
//			.map( atom -> atom.getOriginal() )
//			.filter( e -> e instanceof Atom)
//			.map(e -> ((Atom) e).getPredicate() )
//			.collect(Collectors.toSet()) );

		rule.predicatesInBody()
			.flatMap( predicate -> predicatesToRulesMap.get(predicate).stream() )
			.forEach( arule -> processRule( predicatesToRulesMap, arule, predicatesInPreconditions ) );
//		rule.body()
//			.map( atom -> atom.getOriginal() )
//			.filter( e -> e instanceof Atom)
//			.map(e -> ((Atom) e).getPredicate() )
//			.flatMap( predicate -> predicatesToRulesMap.get(predicate).stream() )
//			.forEach( arule -> processRule( predicatesToRulesMap, arule, predicatesInPreconditions ) );
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public Stream<Constant> constants() {
		return constants.stream();
	}

	public Stream<NormalizedOperator> operators() {
		return operators.stream();
	}

	public Stream<NormalizedRule> rules() {
		return rules.stream();
	}

	public Stream<Predicate> predicates() {
		return allPredicates.stream();
	}

	public Stream<Predicate> immutablePredicates() {
		return immutablePredicates.stream();
	}
	public Stream<Predicate> mutablePredicates() {
		return mutablePredicates.stream();
	}
	public Stream<Predicate> derivedPredicates() {
		return derivedPredicates.stream();
	}
	
	public Set<Constant> getConstants() {
		return constants;
	}
	public Set<NormalizedOperator> getOperators() {
		return operators;
	}
	public ImmutableSet<NormalizedRule> getRules() {
		return rules;
	}
	public Set<Predicate> getPredicates() {
		return allPredicates;
	}
	public Set<Predicate> getImmutablePredicates() {
		return immutablePredicates;
	}
	public Set<Predicate> getMutablePredicates() {
		return mutablePredicates;
	}
	public ImmutableSet<Predicate> getDerivedPredicates() {
		return derivedPredicates;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				constants,
				mutablePredicates,
				immutablePredicates,
				operators
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
		
		NormalizedDomain other = (NormalizedDomain) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.constants, other.constants) &&
				Objects.equals(this.mutablePredicates, other.mutablePredicates) &&
				Objects.equals(this.immutablePredicates, other.immutablePredicates) &&
				Objects.equals(this.operators, other.operators);
	}
	
	public static class Builder {

		Domain originalDomain;
		List<NormalizedOperator> operators = Lists.newArrayList();
		List<Constant> constants = Lists.newArrayList();
		List<NormalizedRule> rules = Lists.newArrayList();
		QualifiedName name;
		
		Builder() {}
		
		public Builder original(Domain original) {
			Objects.requireNonNull(original);
			this.originalDomain = original;
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
		
		public Builder constant(Constant constant) {
			Objects.requireNonNull(constant);
			constants.add(constant);
			return this;
		}
		
		public Builder constants(Iterable<Constant> constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, constants );
			return this;
		}
		
		public Builder constants(Stream<Constant> constants) {
			Objects.requireNonNull(constants);
			this.constants.addAll( constants.collect( Collectors.toList() ));
			return this;
		}

		public Builder constants(Constant... constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, Arrays.asList(constants) );
			return this;
		}

		public Builder operator(NormalizedOperator operator) {
			Objects.requireNonNull(operator);
			operators.add(operator);
			return this;
		}
		
		public Builder operators(Iterable<NormalizedOperator> operators) {
			Objects.requireNonNull(operators);
			Iterables.addAll( this.operators, operators );
			return this;
		}
		
		public Builder operators(Stream<NormalizedOperator> operators) {
			Objects.requireNonNull(operators);
			this.operators.addAll( operators.collect( Collectors.toList() ));
			return this;
		}

		public Builder operators(NormalizedOperator... operators) {
			Objects.requireNonNull(operators);
			Iterables.addAll( this.operators, Arrays.asList(operators) );
			return this;
		}
		
		public Builder rule(NormalizedRule rule) {
			Objects.requireNonNull(rule);
			this.rules.add(rule);
			return this;
		}
		
		public Builder rules(Iterable<NormalizedRule> rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, rules );
			return this;
		}
		
		public Builder rules(Stream<NormalizedRule> rules) {
			Objects.requireNonNull(rules);
			this.rules.addAll( rules.collect( Collectors.toList() ));
			return this;
		}

		public Builder rules(NormalizedRule... rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, Arrays.asList(rules) );
			return this;
		}
		
		public NormalizedDomain build() {
			if( operators.isEmpty() ) {
				throw new IllegalArgumentException("Domain should have at least one operator");
			}

			return new NormalizedDomain(name, originalDomain, constants, operators, rules);
		}
		
	}

}
