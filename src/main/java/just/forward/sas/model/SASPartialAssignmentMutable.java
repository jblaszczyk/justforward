package just.forward.sas.model;

import java.util.Objects;

import just.forward.api.proposition.Literal;

public class SASPartialAssignmentMutable extends AbstractSASPartialAssignment {

	SASPartialAssignmentMutable() {
	}

	SASPartialAssignmentMutable(AbstractSASPartialAssignment other) {
		super(other);
	}

	public static SASPartialAssignmentMutable newInstance() {
		return new SASPartialAssignmentMutable();
	}

	public static SASPartialAssignmentMutable copyOf(AbstractSASPartialAssignment other) {
		return new SASPartialAssignmentMutable(other);
	}
	
	public SASPartialAssignmentImmutable toImmutable() {
		return new SASPartialAssignmentImmutable(this);
	}

	public boolean unify(SASVariable var, Literal sasvalue ) {
		Objects.requireNonNull(var);
		Objects.requireNonNull(sasvalue);
		
		if( map.containsKey(var) ) {
			Literal value = map.get(var);
			return sasvalue.equals(value);
			
		} else {
			map.put(var, sasvalue);
			return true;
		}
	}
	
/*	public boolean unify(SASValue sasvalue, SASVariable var) {
		Objects.requireNonNull(var);
		Objects.requireNonNull(sasvalue);
		return false;
	}
	public boolean unify(SASVariable var1, SASVariable var2 ) {
		Objects.requireNonNull(var1);
		Objects.requireNonNull(var2);
		return false;
	}
*/	
	// TODO: rename to sth else
	public SASPartialAssignmentMutable update(AbstractSASPartialAssignment other) {
		SASPartialAssignmentMutable result = new SASPartialAssignmentMutable(this);
		result.merge(other);
		return result;
	}

	public void merge(AbstractSASPartialAssignment other) {
		for(SASVariable var : other.getVariables()) {
			this.map.put(var, other.findValue(var));
		}
	}
	
	public SASPartialAssignmentMutable duplicate() {
		SASPartialAssignmentMutable result = new SASPartialAssignmentMutable(this);
		return result;
	}
}
