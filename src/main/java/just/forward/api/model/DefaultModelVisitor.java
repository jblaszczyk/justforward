package just.forward.api.model;

public class DefaultModelVisitor implements ModelVisitor {

	@Override
	public boolean enterDomain(Domain domain) {
		return true;
	}

	@Override
	public void leaveDomain(Domain domain) {
		// nothing to do here
	}

	@Override
	public boolean enterProblem(Problem problem) {
		return true;
	}

	@Override
	public void leaveProblem(Problem problem) {
		// nothing to do here
	}

	@Override
	public boolean enterOperator(Operator operator) {
		return true;
	}

	@Override
	public void leaveOperator(Operator operator) {
		// nothing to do here
	}
}
