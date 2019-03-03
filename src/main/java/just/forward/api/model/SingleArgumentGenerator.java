package just.forward.api.model;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import just.forward.api.expression.Constant;
import just.forward.api.expression.Term;
import just.forward.api.expression.Variable;

@Deprecated
public class SingleArgumentGenerator implements Iterator<Term> {
	
	Problem problem;
	Operator operator;
	Variable variable;
	int parameterIndex;
	Set<Constant> arguments;
	
	Iterator<Constant> iterator;
	
	public SingleArgumentGenerator(Problem problem, Operator operator, Variable variable) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(operator);
		Objects.requireNonNull(variable);
		
		if( !operator.getParameters().contains(variable) ) {
			throw new RuntimeException( new IllegalArgumentException() );
		}
		this.problem = problem;
		this.operator = operator;
		this.variable = variable;
		this.parameterIndex = operator.getParameters().indexOf(variable);
		this.arguments = problem.getAllObjects();
		iterator = arguments.iterator();
	}

	public SingleArgumentGenerator(Problem problem, Operator operator, int parameterIndex) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(operator);
		
		if( parameterIndex < 0 || parameterIndex >= operator.getParameters().size() ) {
			throw new RuntimeException( new IllegalArgumentException() );
		}
		
		this.problem = problem;
		this.operator = operator;
		this.parameterIndex = parameterIndex;
		this.variable = operator.getParameters().get(parameterIndex);
		this.arguments = problem.getAllObjects();
		iterator = arguments.iterator();
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
