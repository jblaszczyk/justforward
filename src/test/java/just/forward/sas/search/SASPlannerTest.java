package just.forward.sas.search;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.atom;
import static just.forward.api.common.Sentences.constant;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.predicate;
import static just.forward.api.common.Sentences.then;
import static just.forward.api.common.Sentences.var;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import just.forward.api.common.Predicate;
import just.forward.api.expression.Constant;
import just.forward.api.expression.Variable;
import just.forward.api.model.Domain;
import just.forward.api.model.Operator;
import just.forward.api.model.Problem;
import just.forward.api.proposition.AtomLiteral;
import just.forward.sas.model.SASAction;
import just.forward.sas.model.SASConditionalAssignment;
import just.forward.sas.model.SASDomain;
import just.forward.sas.model.SASPartialAssignmentMutable;
import just.forward.sas.model.SASProblem;
import just.forward.sas.model.SASVariable;
import just.forward.sas.model.SASVariableDomain;
import just.forward.sas.search.AStarSASStrategy;
import just.forward.sas.search.NaiveSASHeuristics;
import just.forward.sas.search.NaiveSASSearchNodeSuccessorGenerator;
import just.forward.sas.search.SASPlanner;
import just.forward.sas.search.SASSearchNode;
import just.forward.search.Heuristics;

@SuppressWarnings("unused")
public class SASPlannerTest {

	@Test
	public void lightbulb() throws Exception {
		given:;
			
			Variable var = var("x");
			Predicate predicateOn = predicate("on", var);
			Predicate predicateOff = predicate("off", var);
			AtomLiteral on = atom(predicateOn, var);
			AtomLiteral off = atom(predicateOff, var);
			
			Operator switchOnOperator = Operator.builder()
				.name("switch_on")
				.precondition(and(not(on), off))
				.effect(and(then(on), then(not(off))))
				.parameter(var)
			.build();
			
			Operator switchOffOperator = Operator.builder()
				.name("switch_off")
				.precondition(and(on, not(off)))
				.effect(and(then(not(on)), then(off)))
				.parameter(var)
			.build();
			
			Constant lightbulb = constant("lightbulb");

			AtomLiteral init = atom(predicateOff, lightbulb);
			AtomLiteral goal = atom(predicateOn, lightbulb);
			
			Domain domain = Domain.builder()
				.name("domain")
//				.predicate(predicateOn)
//				.predicate(predicateOff)
				.operator(switchOnOperator)
				.operator(switchOffOperator)
			.build();
			
			Problem problem = Problem.builder(domain)
				.name("problem")
				.object(lightbulb)
				.init(then(init))
				.goal(goal)
			.build();
			
//			AgnosticToSASTranslator translator = new AgnosticToSASTranslator(problem);
//			SASProblem sasProblem = translator.translate();
			

//			SASAtomValue sasfdr1valOn = SASAtomValue.of(goal);
//			SASAtomValue sasfdr1valOff = SASAtomValue.of(init);
			SASVariableDomain sasfdr1 = SASVariableDomain.builder()
//				.sasAtom(sasfdr1valOn)
//				.sasAtom(sasfdr1valOff)
				.atom(goal)
				.atom(init)
			.build();
			SASVariable sasvar1 = SASVariable.newInstance("sasvar1", sasfdr1);

			SASPartialAssignmentMutable switchOnSasActionPreconditions = SASPartialAssignmentMutable.newInstance();
			switchOnSasActionPreconditions.unify(sasvar1, init);
			
			SASPartialAssignmentMutable switchOnSasActionEffectAssignment = SASPartialAssignmentMutable.newInstance();
			switchOnSasActionEffectAssignment.unify(sasvar1, goal);
			SASConditionalAssignment switchOnSasActionEffect = SASConditionalAssignment.newInstance(SASPartialAssignmentMutable.newInstance(), switchOnSasActionEffectAssignment);
			
			SASAction switchOnSasAction = SASAction.builder()
				.name("switch_on")
				.preconditions(switchOnSasActionPreconditions)
				.effect(switchOnSasActionEffect)
			.build();

			SASPartialAssignmentMutable switchOffSasActionPreconditions = SASPartialAssignmentMutable.newInstance();
			switchOffSasActionPreconditions.unify(sasvar1, goal);
			
			SASPartialAssignmentMutable switchOffSasActionEffectAssignment = SASPartialAssignmentMutable.newInstance();
			switchOffSasActionEffectAssignment.unify(sasvar1, init);
			SASConditionalAssignment switchOffSasActionEffect = SASConditionalAssignment.newInstance(SASPartialAssignmentMutable.newInstance(), switchOffSasActionEffectAssignment);

			SASAction switchOffSasAction = SASAction.builder()
				.name("switch_off")
				.preconditions(switchOffSasActionPreconditions)
				.effect(switchOffSasActionEffect)
			.build();
			
			SASPartialAssignmentMutable sasProblemInit = SASPartialAssignmentMutable.newInstance();
			sasProblemInit.unify(sasvar1, init);
			SASPartialAssignmentMutable sasProblemGoal = SASPartialAssignmentMutable.newInstance();
			sasProblemGoal.unify(sasvar1, goal);
			
			SASDomain sasDomain = SASDomain.builder()
				.name(domain.getName())
				.action(switchOnSasAction)
				.action(switchOffSasAction)
			.build();
			
			SASProblem sasProblem = SASProblem.builder(sasDomain)
				.name(problem.getName())
				.init(sasProblemInit)
				.goal(sasProblemGoal)
			.build();
			
			Heuristics<SASSearchNode> heuristics = NaiveSASHeuristics.newInstance(sasProblem);
			
		when:;
			SASPlanner planner = SASPlanner.newInstance(sasProblem, AStarSASStrategy.newInstance(heuristics), NaiveSASSearchNodeSuccessorGenerator.newInstance(sasProblem));
			List<SASAction> plan = planner.plan();
			
		then:;
			assertThat( plan ).isNotNull();
			assertThat( plan ).isNotEmpty();
			assertThat( plan ).hasSize(1);
			
			System.out.println(plan);
	}
	
}
