package just.forward.sas.model;

import javax.annotation.concurrent.Immutable;

import just.forward.api.proposition.Literal;

@Immutable
public final class SASVariableAssignmentImmutable extends SASVariableAssignmentMutable {

	public SASVariableAssignmentImmutable(SASVariable variable, Literal value) {
		super(variable, value);
	}

	public static SASVariableAssignmentImmutable of(SASVariable variable, Literal value) {
		return new SASVariableAssignmentImmutable(variable, value);
	}
	
	public static SASVariableAssignmentImmutable copyOf(SASVariableAssignmentMutable other) {
		if( other instanceof SASVariableAssignmentImmutable ) {
			return (SASVariableAssignmentImmutable) other;
		}
		
		return of(other.getVariable(), other.getValue());
	}
	
	@Override
	public Literal setValue(Literal value) {
		throw new UnsupportedOperationException();
	}
	
	public SASVariableAssignmentMutable toMutable() {
		return SASVariableAssignmentMutable.of(getVariable(), getValue());
	}
	
}
