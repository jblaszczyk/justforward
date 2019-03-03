package just.forward.sas.model;

import java.util.Map;
import java.util.Objects;

import just.forward.api.proposition.Literal;

public class SASVariableAssignmentMutable implements Map.Entry<SASVariable, Literal> {

	SASVariable variable;
	Literal value;
	
	protected SASVariableAssignmentMutable(SASVariable variable, Literal value) {
		Objects.requireNonNull(variable);
		Objects.requireNonNull(value);
		this.variable = variable;
		this.value = value;
	}
	
	public static SASVariableAssignmentMutable of(SASVariable variable, Literal value) {
		return new SASVariableAssignmentMutable(variable, value);
	}
	
	public static SASVariableAssignmentMutable copyOf(SASVariableAssignmentMutable other) {
		return of(other.getVariable(), other.getValue());
	}
	
	@Override
	public SASVariable getKey() {
		return variable;
	}

	public SASVariable getVariable() {
		return variable;
	}
	
	@Override
	public Literal getValue() {
		return value;
	}

	@Override
	public Literal setValue(Literal value) {
		Literal oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	
	public SASVariableAssignmentImmutable toImmutable() {
		return SASVariableAssignmentImmutable.of(getVariable(), getValue());
	}
	
}
