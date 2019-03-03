package just.forward.sas.model;

import java.util.Objects;

import javax.annotation.concurrent.Immutable;

@Immutable
public class SASConditionalAssignment {

	SASPartialAssignmentImmutable condition;
	SASPartialAssignmentImmutable assignment;
	
	SASConditionalAssignment(AbstractSASPartialAssignment condition, AbstractSASPartialAssignment assignment) {
		this.condition = SASPartialAssignmentImmutable.copyOf(condition);
		this.assignment = SASPartialAssignmentImmutable.copyOf(assignment);
	}
	
	public static SASConditionalAssignment newInstance(AbstractSASPartialAssignment condition, AbstractSASPartialAssignment assignment) {
		return new SASConditionalAssignment(condition, assignment);
	}
	
	public AbstractSASPartialAssignment getCondition() {
		return condition;
	}
	
	public AbstractSASPartialAssignment getAssignment() {
		return assignment;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				condition,
				assignment
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		SASConditionalAssignment other = (SASConditionalAssignment) obj;
		return Objects.equals(this.condition, other.condition) &&
				Objects.equals(this.assignment, other.assignment);
	}
	
}
