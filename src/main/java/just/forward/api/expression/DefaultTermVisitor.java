package just.forward.api.expression;

public class DefaultTermVisitor implements TermVisitor {

	@Override
	public boolean enterVariable(Variable variable) {
		return true;
	}

	@Override
	public boolean enterConstant(Constant constant) {
		return true;
	}

	@Override
	public boolean enterFluentTerm(FluentTerm fluentTerm) {
		return true;
	}
	
	@Override
	public void leaveVariable(Variable variable) {
		// nothing to do here
	}

	@Override
	public void leaveConstant(Constant constant) {
		// nothing to do here
	}
	
	@Override
	public void leaveFluentTerm(FluentTerm fluentTerm) {
		// nothing to do here
	}

	@Override
	public boolean enterTuple(Tuple tuple) {
		return true;
	}

	@Override
	public void leaveTuple(Tuple tuple) {
		// nothing to do here
	}
}
