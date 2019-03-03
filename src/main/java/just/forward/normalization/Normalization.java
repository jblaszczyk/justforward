package just.forward.normalization;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import just.forward.api.common.Namespace;
import just.forward.api.common.Predicate;
import just.forward.api.common.QualifiedName;
import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.model.Domain;
import just.forward.api.model.Operator;
import just.forward.api.model.Problem;
import just.forward.api.proposition.AbstractCompoundProposition;
import just.forward.api.proposition.AndProposition;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.ExistsProposition;
import just.forward.api.proposition.ForAllProposition;
import just.forward.api.proposition.Literal;
import just.forward.api.proposition.NotProposition;
import just.forward.api.proposition.OrProposition;
import just.forward.api.proposition.Proposition;
import just.forward.api.proposition.Rule;
import just.forward.api.statement.AndStatement;
import just.forward.api.statement.Assignment;
import just.forward.api.statement.BooleanAssignment;
import just.forward.api.statement.FluentAssignment;
import just.forward.api.statement.ForAllStatement;
import just.forward.api.statement.Statement;
import just.forward.api.statement.WhenStatement;
import just.forward.normalization.model.NormalizedDomain;
import just.forward.normalization.model.NormalizedEffect;
import just.forward.normalization.model.NormalizedOperator;
import just.forward.normalization.model.NormalizedProblem;
import just.forward.normalization.model.NormalizedRule;

/**
 * see: M. Helmert "Concise finite-domain representations for PDDL planning tasks" (p.13)
 */
public class Normalization {

	Map<Proposition, Rule> syntheticRules = Maps.newHashMap();
	Set<Predicate> syntheticPredicates = Sets.newHashSet();
	
	Normalization() {
	}
	
	public static Normalization newInstance() {
		return new Normalization();
	}

	public Stream<NormalizedEffect> effect(Statement effect) {
		return convertStatement( normalizeEffect( effect ) );
	}
	
	public Stream<Literal> init(Statement init) {
		return convertInit( normalizeEffect(init) );
	}
	
	public Stream<Literal> goal(Proposition goal) {
		return convertProposition( normalizeGoal(goal) );
	}
	
	public Stream<Stream<Literal>> precondition(Proposition precondition) {
		return normalizePrecondition(precondition).stream()
			.map(this::convertProposition);
	}
	
	Stream<Literal> convertProposition(Proposition proposition) {
		if( proposition.uses( OrProposition.class ) ) {
			throw new AssertionError("Wrong structure: " + proposition.getClass());
		}
		if( proposition.uses( NotProposition.class ) ) { // should have been NNF
			throw new AssertionError("Wrong structure: " + proposition.getClass());
		}
		if( proposition.uses( ForAllProposition.class ) ) { // should have been replaced by rules
			throw new AssertionError("Wrong structure: " + proposition.getClass());
		}

		if( proposition instanceof AndProposition ) {
			return convertAndProposition((AndProposition) proposition);
		}
		
		if( proposition instanceof Literal ) {
			return Stream.of( convertLiteral((Literal) proposition) );
		}
		
		throw new AssertionError();
	}

	Stream<Literal> convertAndProposition(AndProposition proposition) {
		if( proposition.contains(AndProposition.class) ) {
			if( proposition.uses( ForAllProposition.class ) ) { // should have been DNF already
				throw new AssertionError("Wrong structure: " + proposition.getClass());
			}
		}

		return proposition.children()
			.map(e -> (Literal) e)
			.map(this::convertLiteral);
	}

	Literal convertLiteral(Literal literal) {
		return literal;
	}

	public Stream<NormalizedOperator> operator(Operator operator) {
		
		return precondition(operator.getPrecondition())
			.map(stream -> {
				NormalizedOperator.Builder builder = NormalizedOperator.builder();
				builder.original(operator);
				builder.name( operator.getName() );
				builder.parameters( operator.parameters() );
				builder.effects( effect( operator.getEffect() ));
				builder.preconditions(stream);
				
				return builder.build();
			});
	}
	
	public Stream<NormalizedRule> rule(Rule rule) {
		return precondition( rule.getBody() )
			.map( stream -> { 
				NormalizedRule.Builder builder = NormalizedRule.builder( rule.getPredicate() );
				builder.original(rule);
				builder.atoms( stream );
				return builder.build();
			});
	}
	
	public NormalizedDomain domain(Domain domain) {
		NormalizedDomain.Builder builder = NormalizedDomain.builder();
		builder.original(domain);
		builder.name( domain.getName() );
		builder.constants( domain.constants() );
		
		domain.operators()
			.flatMap(this::operator)
			.forEach(builder::operator);
		
		domain.rules()
			.flatMap(this::rule)
			.forEach(builder::rule);
		
		syntheticRules.values().stream()
			.flatMap(this::rule)
			.forEach(builder::rule);
		
		return builder.build();
	}

	public NormalizedProblem problem(Problem problem) {
		NormalizedProblem.Builder builder =  NormalizedProblem.builder( domain(problem.getDomain()) );

		builder.original(problem);
		builder.name( problem.getName() );
		builder.init( init( problem.getInit() ) );
		builder.goal( goal( problem.getGoal() ) );
		builder.objects( problem.objects() );
		
		return builder.build();
	}
	
/*
 	goal:
 	0. imp/iff/xor to simple form or/and/not
 	1. replace top/outermost forall-s with axioms (not toPNF!)
 	2. goal: if contains/uses exists/or then axiom
*/
	Proposition normalizeGoal(Proposition goal) {
		Proposition newGoal = goal.toNNF().toSimplified();

		// 	1. replace top/outermost forall-s with axioms (not toPNF!)
		newGoal = replaceForAll(newGoal).toSimplified();
		
		// 	2. goal: if contains/uses exists/or then axiom
		if( newGoal.uses(OrProposition.class) || newGoal.uses(ExistsProposition.class) ) {
			newGoal = wrapInRule(newGoal).toSimplified();
		}
		
		return newGoal;
	}
	
/*
    preconditions:
 	0. imp/iff/xor to simple form or/and/not
 	1. replace top/outermost forall-s with axioms (not toPNF!)
 	2. toDNF
 	3. split operator
	4. toPNF
*/
	Set<Proposition> normalizePrecondition(Proposition arg) {
		Proposition newPrecondition = arg.toNNF();

		// 	1. replace top/outermost forall-s with axioms (not toPNF!)
		newPrecondition = replaceForAll(newPrecondition);
	 	
		// 2. toDNF
		newPrecondition = newPrecondition.toDNF();

		// 3. split operator // TODO may simplify if enough tests are written
		Set<Proposition> result = Sets.newHashSet();
		if( newPrecondition instanceof OrProposition ) {
			OrProposition or = (OrProposition) newPrecondition;
			result.addAll( or.getChildren() );
			
		} else if( newPrecondition instanceof AndProposition ) {
			result.add(newPrecondition);
			
		} else if( newPrecondition instanceof Literal ) {
			result.add(newPrecondition);
			
		} else if( newPrecondition instanceof ExistsProposition ) {
			if( newPrecondition.contains(OrProposition.class) ) {
				throw new AssertionError("OrProposition shoulhd have been dealt with by now");
			}

			result.add(newPrecondition);
		}

		// 4. toPNF
		result = result.stream()
			.map(Proposition::toPNF)
			.map(Proposition::toSimplified)
			.collect(Collectors.toSet());
		
		return result;
	}

/*
 	effects:
 	1. push forall (distribute over "and"): forall(x, p and q) ==> forall(x,p) and forall(x,q)
 	2. toWhenNF() (getWhen-or to getWhen-and): when(p or q,e) ==> when(p,e) and when(q,e) 
 	4. toWhenNF() (distribute when over and): when(phi,p and q) ==> when(phi,p) and when(phi,q)
 	5. toWhenNF() (pull forall): for each conditional toPNF: when(phi, forall(x,p)) ==> forall(x,when(phi,p)) 
 	8. toWhenNF() (flatten): when(phi,when(ksi,e)) ==> when(phi and ksi,e)
 	6. toSimplified(): flatten nested (and) effects: and(and(p,q),and(r,s)) => and(p,q,r,s)
	3. ?getWhen().toPNF
 	7. each and's child: toPNF(): collect quantified vars
 	9. result is: AndStatement (conjunctive effect) > ForAllStatement (universal effect) > WhenStatement (conditional effect) > Assignment(simple effect)
*/
	Statement normalizeEffect(Statement arg) {
		if( arg.contains(ExistsProposition.class) ) {
			throw new IllegalArgumentException("ExistsProposition not supported even in WhenSentence.getWhen()");
		}

		// 1. push forall (distribute over "and"): forall(x, p and q) ==> forall(x,p) and forall(x,q)
		Statement result = arg.toCNF();
		
		// 2. toWhenNF() (getWhen-or to getWhen-and): when(p or q,e) ==> when(p,e) and when(q,e) 
 		// 4. toWhenNF() (distribute when over and): when(phi,p and q) ==> when(phi,p) and when(phi,q)
 		// 5. toWhenNF() (pull forall): for each conditional toPNF: when(phi, forall(x,p)) ==> forall(x,when(phi,p)) 
 		// 8. toWhenNF() (flatten): when(phi,when(ksi,e)) ==> when(phi and ksi,e)
		// 3. toWhenNF() getWhen().toPNF
		result = result.toWhenNF();
		
 		// 6. toSimplified(): flatten nested (and) effects: and(and(p,q),and(r,s)) => and(p,q,r,s)
//		result = result.simplify();
		
 		// 7. each and's child: toPNF(): collect quantified vars
		// TODO
		if( result instanceof AndStatement ) {
			AndStatement and = (AndStatement) result;
			
			return AndStatement.newInstance(
				and.children()
					.map(Statement::toPNF)
			);
		}
		
		return result;
		// 9. result is: AndStatement (conjunctive effect) > ForAllStatement (universal effect) > WhenStatement (conditional effect) > Assignment(simple effect)
	}
	
	Stream<NormalizedEffect> convertStatement(Statement arg) {
		if( arg instanceof AndStatement ) {
			return convertAndStatement((AndStatement) arg);
		}
		
		return Stream.of(convertOneStatement(arg));
	}

	Stream<NormalizedEffect> convertAndStatement(AndStatement arg) {
		return arg.children()
			.map(this::convertOneStatement);
	}
	
	NormalizedEffect convertOneStatement(Statement arg) {
		if( arg instanceof ForAllStatement ) {
			return convertForAllStatement((ForAllStatement) arg);
			
		} else if( arg instanceof WhenStatement ) {
			return convertWhenStatement((WhenStatement) arg, ImmutableSet.of());
			
		} else if( arg instanceof Assignment ) {
			return convertAssignment((Assignment) arg, ImmutableSet.of(), ImmutableSet.of());
		}
		
		throw new AssertionError("Wrong structure: " + arg.getClass());
	}

	NormalizedEffect convertForAllStatement(ForAllStatement arg) {
		Statement child = arg.getChild();
		Set<Variable> vars = arg.getQuantifiedVariables();
		
		if( child instanceof WhenStatement ) {
			return convertWhenStatement((WhenStatement) child, vars);
		
		} else if( child instanceof Assignment ) {
			return convertAssignment((Assignment) child, vars, ImmutableSet.of());
		}
	
		throw new AssertionError("Wrong structure: " + child.getClass());
	}

	NormalizedEffect convertWhenStatement(WhenStatement arg, Set<Variable> universallyQuantifiedVars) {
		Statement then = arg.getThen();
		Set<Literal> conditions = extractLiterals( arg.getWhen() ) // toCNF()?
			.collect(Collectors.toSet());
		
		if( then instanceof Assignment ) {
			return convertAssignment((Assignment) then, universallyQuantifiedVars, conditions);
		}
	
		throw new AssertionError("Wrong structure: " + then.getClass());
	}
	
	Stream<Literal> extractLiterals(Proposition arg) {
		if( arg instanceof Literal ) {
			Literal literal = (Literal) arg;
			return Stream.of(literal);
			
		} else if( arg instanceof AndProposition ) {
			AndProposition and = (AndProposition) arg;
			return and.children()
				.flatMap(this::extractLiterals);
		}
		
		throw new AssertionError("Wrong structure: " + arg.getClass());
	}

	NormalizedEffect convertAssignment(Assignment arg, Set<Variable> universallyQuantifiedVars, Set<Literal> conditions) {
		if( arg instanceof BooleanAssignment) {
			return convertBooleanAssignment((BooleanAssignment) arg, universallyQuantifiedVars, conditions);
			
		} else if( arg instanceof FluentAssignment ) {
			throw new AssertionError("Not implemented yet.");
		}
		
		throw new AssertionError("Wrong structure: " + arg.getClass());
	}

	NormalizedEffect convertBooleanAssignment(BooleanAssignment arg, Set<Variable> universallyQuantifiedVars, Set<Literal> conditions) {
		return NormalizedEffect.builder()
			.variables(universallyQuantifiedVars)
			.conditions(conditions)
			.effect(arg.getAtom())
			.build();
	}

	Stream<Literal> convertInit(Statement init) {
		if( init.uses(WhenStatement.class) ) {
			throw new AssertionError("Init statement cannot contain WhenStatement");
		}

		if( init.uses(ForAllStatement.class) ) {
			throw new AssertionError("ForAllStatement in init not yet supported.");
		}

		return convertStatement(init)
			.flatMap(e -> e.effects());
	}


	@Deprecated
	Proposition simplifyProposition(Proposition arg) {
		Proposition input = arg.toNNF().toPNF().toSimplified(); // not toPNF() at this stage!
		return input;
	}
	
	Stream<Proposition> collectForAll(Proposition arg) {
		if(arg instanceof ForAllProposition) {
			return Stream.of(arg);
		}
		
		if( arg instanceof AbstractCompoundProposition ) {
			AbstractCompoundProposition compound = (AbstractCompoundProposition) arg;
			return compound.children()
				.flatMap(e -> collectForAll(e));
		}
		
		return Stream.of();
	}
	
	AtomLiteral replacementForForAll(Proposition arg) {
		if( !(arg instanceof ForAllProposition) ) {
			throw new AssertionError();
		}
		
		ForAllProposition forall = (ForAllProposition) arg;
		ExistsProposition notForall = forall.toNegated();
		return wrapInRule(notForall).toNegated();
	}
	
	Proposition replaceForAll(Proposition arg) {
		if( arg.uses(ForAllProposition.class) ) {
			Substitution.Builder substitutionBuilder = Substitution.builder();
			collectForAll(arg).forEach( e -> {
				substitutionBuilder.map(e, replacementForForAll(e));
			});
			
			Substitution substitution = substitutionBuilder.build();
			return arg.substitute(substitution);
		}
	
		return arg;
	}
	
	Predicate newSyntheticPredicate(Stream<Variable> parameters) {
		Predicate predicate = Predicate.newSyntheticInstance(
			QualifiedName.of( Namespace.getHidden(), Integer.valueOf(syntheticPredicates.size()).toString() ),
			parameters
		);
		syntheticPredicates.add(predicate);
		return predicate;
	}
	
	AtomLiteral wrapInRule(Proposition body) {
		Stream<Variable> parameters = body.freeVariables()
			.distinct();
		Predicate predicate = newSyntheticPredicate(parameters);
		Rule rule = Rule.newSyntheticInstance(predicate, body);
		syntheticRules.put(body, rule);
		return AtomLiteral.newInstance(predicate, parameters);
	}
}
