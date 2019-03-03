package just.forward.api;

public enum Capabilities {

	PRECONDITIONS_NEGATIVE, // :negative-preconditions (Allow "not" in goal descriptions
	PRECONDITIONS_DISJUNCTION, // :disjunctive-preconditions (Allow "or" in goal descriptions)
	PRECONDITIONS_EXISTS, // :existential-preconditions (Allow "exists" in goal descriptions)
	PRECONDITIONS_FORALL, // :universal-preconditions (Allow "forall" in goal descriptions)
	EFFECTS_FORALL, 
	EFFECTS_WHEN, // :conditional-effects (Allow "when" in action effects)
	RULES, // :derived-predicates
	
	TYPES, // :typing (Allow type names in declarations of variables)
	EQUALITY, // :equality (Support "=" as built-in predicate)
	EXPRESSION_EVAL, // :expression-evaluation (Support "eval" predicate in axioms (implies :domain-axioms))
	FLUENTS_OBJECT, // :fluents Support type (fluent t). (Implies :expression-evaluation)
	FLUENTS_INTEGER,
	FLUENTS_DECIMAL,

	AXIOM, // :domain-axioms (Allow domains to have :axioms)
	
	// action costs
	// plan metrics
	// :durative-actions (does not imply :fluents)
	// :timed-initial-literals
	
	// :action-expansions (Allow actions to have :expansions) (htn?)
	// :foreach-expansions (Allow actions expansions to use "foreach (implies :action-expansions)")
	// :dag-expansions (Allow labeled subactions (implies :action-expansions))
	// :subgoal-through-axioms (Given axioms p ⊃ q and goal q, generate subgoal p)
	// :safety-constraints (Allow :safety conditions for a domain)
	// :open-world (Don’t make the "closed-world assumption" for all predicates - i.e., if an atomic formula is not known to be true, it is not necessarily assumed false)
	// :true-negation (Don’t handle not using negation as failure, but treat it as in first-order logic) (implies :open-world)
	
}

/*
* :strips (Basic STRIPS-style adds and deletes)
* :typing (Allow type names in declarations of variables)
* :disjunctive-preconditions (Allow "or" in goal descriptions)
* :equality (Support "=" as built-in predicate)
* :existential-preconditions (Allow "exists" in goal descriptions)
* :universal-preconditions (Allow "forall" in goal descriptions)
* :quantified-preconditions ( :existential-preconditions + :universal-preconditions)
* :conditional-effects (Allow "when" in action effects)
* :action-expansions (Allow actions to have :expansions) (htn?)
* :foreach-expansions (Allow actions expansions to use "foreach (implies :action-expansions)")
* :dag-expansions (Allow labeled subactions (implies :action-expansions))
* :domain-axioms (Allow domains to have :axioms)
* :subgoal-through-axioms (Given axioms p ⊃ q and goal q, generate subgoal p)
* :safety-constraints (Allow :safety conditions for a domain)
* :expression-evaluation (Support "eval" predicate in axioms (implies :domain-axioms))
* :fluents Support type (fluent t). (Implies :expression-evaluation)
* :open-world (Don’t make the "closed-world assumption" for all predicates - i.e., if an atomic formula is not known to be true, it is not necessarily assumed false)
* :true-negation (Don’t handle not using negation as failure, but treat it as in first-order logic) (implies :open-world)
* :adl = (:strips + :typing + :disjunctive-preconditions + :equality + :quantified-preconditions + :conditional-effects)
* :ucpop = (:adl + :domain-axioms + :safety-constraints)

PDDL2.2
* :negative-preconditions (Allow "not" in goal descriptions)
* :derived-predicates
* :durative-actions (does not imply :fluents)\
* :timed-initial-literals
*/