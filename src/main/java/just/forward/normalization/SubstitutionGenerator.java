package just.forward.normalization;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import just.forward.api.common.Parameters;
import just.forward.api.expression.Term;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.normalization.model.NormalizedProblem;

public class SubstitutionGenerator implements Iterator<VariableSubstitution>, Iterable<VariableSubstitution> {

	NormalizedProblem problem;
	Variable[] parameters;
	SingleArgumentGenerator[] generators;
	Term[] currentArguments;
	boolean firstTime;

	private SubstitutionGenerator(NormalizedProblem problem, List<Variable> variables) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(variables);
		
		this.problem = problem;
		this.parameters = new Variable[variables.size()];
		variables.toArray(this.parameters);
		this.currentArguments = new Term[parameters.length];
		this.generators = new SingleArgumentGenerator[parameters.length];

		reset();
	}

	public static  SubstitutionGenerator newInstance(NormalizedProblem problem, List<Variable> variables) {
		return new SubstitutionGenerator(problem, variables);
	}
	
	public void reset() {
		for( int i = 0; i < parameters.length; i++ ) {
			generators[i] = SingleArgumentGenerator.newInstance(problem, parameters[i]);
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
