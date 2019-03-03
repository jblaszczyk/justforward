package just.forward.api.model;

import java.util.Iterator;
import java.util.Objects;

import just.forward.api.expression.Term;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;

@Deprecated
public class SubstitutionGenerator implements Iterator<VariableSubstitution>, Iterable<VariableSubstitution> {

	Problem problem;
	Operator operator;
	Variable[] parameters;
	SingleArgumentGenerator[] generators;
	Term[] currentArguments;
	boolean firstTime;

	public SubstitutionGenerator(Problem problem, Operator operator) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(operator);
		
		this.problem = problem;
		this.operator = operator;
		this.parameters = new Variable[operator.getParameters().size()];
		operator.getParameters().toArray(this.parameters);
		this.currentArguments = new Term[parameters.length];
		this.generators = new SingleArgumentGenerator[parameters.length];

		reset();
	}

	public void reset() {
		for( int i = 0; i < parameters.length; i++ ) {
			generators[i] = new SingleArgumentGenerator(problem, operator, i);
			if( generators[i].hasNext() ) {
				currentArguments[i] = generators[i].next();
			}
		}
		
		firstTime = true;
	}
	
	@Override
	public boolean hasNext() {
		if( firstTime ) {
			for( int i = parameters.length - 1; i >= 0; i-- ) {
				if( currentArguments[i] == null ) {
					return false;
				}
			}
			
			return true;
			
		} else {
			for( int i = parameters.length - 1; i >= 0; i-- ) {
				if( generators[i].hasNext() ) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public VariableSubstitution next() {
		if( firstTime ) {
			firstTime = false;

		} else {

			for( int i = parameters.length - 1; i >= 0; i-- ) {
				if( generators[i].hasNext() ) {
					currentArguments[i] = generators[i].next();
					break;
					
				} else {
					generators[i].reset();
					currentArguments[i] = generators[i].next();
				}
			}
		}
		
		VariableSubstitution.Builder substitution = VariableSubstitution.builder();
		for( int i = 0; i < parameters.length; i++ ) {
			substitution.map( parameters[i], currentArguments[i] );
		}
		
		return substitution.build();
	}

	@Override
	public Iterator<VariableSubstitution> iterator() {
		return this;
	}
	
}
