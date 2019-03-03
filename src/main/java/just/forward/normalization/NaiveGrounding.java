package just.forward.normalization;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import just.forward.api.expression.VariableSubstitution;
import just.forward.normalization.model.GroundNormalizedAction;
import just.forward.normalization.model.GroundNormalizedRule;
import just.forward.normalization.model.GroundNormalizedTask;
import just.forward.normalization.model.NormalizedOperator;
import just.forward.normalization.model.NormalizedProblem;
import just.forward.normalization.model.NormalizedRule;

public class NaiveGrounding {

	NaiveGrounding() {}
	
	public static NaiveGrounding newInstance() {
		return new NaiveGrounding();
	}
	
	public GroundNormalizedTask ground(NormalizedProblem problem) {
		GroundNormalizedTask.Builder builder = GroundNormalizedTask.builder(problem);
		problem.getDomain().operators()
			.flatMap( e -> groundOperator(problem, e) )
			.forEach(builder::action);

		problem.getDomain().rules()
			.flatMap( e -> groundRule(problem, e) )
			.forEach(builder::rule);
		
		return builder.build();
	}
	
	Stream<GroundNormalizedAction> groundOperator(NormalizedProblem problem, NormalizedOperator operator) {
		SubstitutionGenerator generator = SubstitutionGenerator.newInstance(problem, operator.getParametersStuct().getVariables());
		ArrayList<VariableSubstitution> substitutions = Lists.newArrayList();
		while( generator.hasNext() ) {
			substitutions.add( generator.next() );
		}
		
		return substitutions.stream()
			.map(e -> operator.ground(e));
	}

	Stream<GroundNormalizedRule> groundRule(NormalizedProblem problem, NormalizedRule rule) {
		SubstitutionGenerator generator = SubstitutionGenerator.newInstance(problem, rule.getParametersStruct().getVariables());
		ArrayList<VariableSubstitution> substitutions = Lists.newArrayList();
		while( generator.hasNext() ) {
			substitutions.add( generator.next() );
		}
		
		return substitutions.stream()
			.map(e -> rule.ground(e));
	}
}
