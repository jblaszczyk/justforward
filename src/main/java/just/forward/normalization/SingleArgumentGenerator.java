package just.forward.normalization;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import just.forward.api.expression.Constant;
import just.forward.api.expression.Term;
import just.forward.api.expression.Variable;
import just.forward.normalization.model.NormalizedOperator;
import just.forward.normalization.model.NormalizedProblem;
import just.forward.util.ImmutableCollectors;

public class SingleArgumentGenerator implements Iterator<Term> {
	
	Variable variable;
	ImmutableSet<Constant> arguments;
	
	Iterator<Constant> iterator;
	
	private SingleArgumentGenerator(NormalizedProblem problem, Variable variable) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(variable);
		
		this.variable = variable;
		this.arguments = problem.findForVariable(variable)
			.collect(ImmutableCollectors.toSet());
		iterator = arguments.iterator();
	}

	public static SingleArgumentGenerator newInstance(NormalizedProblem problem, Variable variable) {
		return new SingleArgumentGenerator(problem, variable);
	}
	
	public void reset() {
		iterator = arguments.iterator();
	}
	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Term next() {
		return iterator.next();
	}
}
