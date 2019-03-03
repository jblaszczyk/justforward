package just.forward.sas.model;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

@Immutable
final public class SASPartialAssignmentImmutable extends AbstractSASPartialAssignment {
	
	SASPartialAssignmentImmutable(AbstractSASPartialAssignment other) {
		super(other);
		this.map = ImmutableMap.copyOf(this.map);
	}

	public static SASPartialAssignmentImmutable copyOf(AbstractSASPartialAssignment other) {
		if( other instanceof SASPartialAssignmentImmutable ) {
			return (SASPartialAssignmentImmutable) other;
		}
		
		return new SASPartialAssignmentImmutable(other);
	}

	public SASPartialAssignmentMutable toMutable() {
		return new SASPartialAssignmentMutable(this);
	}
}