package just.forward.api.model;

import static just.forward.api.common.Sentences.atom;
import static just.forward.api.common.Sentences.constant;
import static just.forward.api.common.Sentences.predicate;
import static just.forward.api.common.Sentences.then;
import static just.forward.api.common.Sentences.var;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Matchers;

import just.forward.api.common.Predicate;
import just.forward.api.expression.Constant;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.AtomLiteral;
import just.forward.test.JustTest;

public class VisitorTest extends JustTest {

	@Test
	@SuppressWarnings("unused")
	public void test() throws Exception {
		given:;
			ModelVisitor problemVisitor = spy(new DefaultModelVisitor());
			
			Constant constant = constant("constant");
			
			Variable param1 = var("param1");
			Predicate predicate1 = predicate("predicate1", param1);
			
			Variable param2 = var("param2");
			Predicate predicate2 = predicate("predicate2", param2);
			
			Variable param3 = var("param2");
			AtomLiteral atom1 = atom(predicate1, param3);
			AtomLiteral atom2 = atom(predicate2, param3);
			Operator operator = Operator.builder().name("operator").precondition(atom1).effect(then(atom2)).parameter(param3).build();
			
			Constant object = constant("object");
			AtomLiteral init = atom(predicate1, constant);
			AtomLiteral goal = atom(predicate2, constant);
			
			Domain domain = Domain.builder()
				.name("domain")
				.constant(constant)
//				.predicate(predicate1)
//				.predicate(predicate2)
				.operator(operator)
			.build();

			Problem problem = Problem.builder(domain)
				.name("problem")
				.init(then(init))
				.goal(goal)
				.object(object)
			.build();
			
		when:;
			problem.accept(problemVisitor);
			
		then:;
			verify( problemVisitor, times(1) ).enterProblem( Matchers.any(Problem.class) );
			verify( problemVisitor, times(1) ).enterProblem( Matchers.eq(problem) );
			verify( problemVisitor, times(1) ).leaveProblem( Matchers.eq(problem) );
			
			verify( problemVisitor, times(1) ).enterDomain(domain);
			verify( problemVisitor, times(1) ).leaveDomain(domain);
			
			verify( problemVisitor, times(1) ).enterOperator(operator);
			verify( problemVisitor, times(1) ).leaveOperator(operator);
	}
}
