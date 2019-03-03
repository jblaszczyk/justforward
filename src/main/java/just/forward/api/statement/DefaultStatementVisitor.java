package just.forward.api.statement;

public class DefaultStatementVisitor implements StatementVisitor {
	
	@Override
	public boolean enterAndStatement(AndStatement arg) {
		return true;
	}

	@Override
	public boolean enterBooleanAssignment(BooleanAssignment arg) {
		return true;
	}
	
	@Override
	public boolean enterEmptyStatement(EmptyStatement arg) {
		return true;
	}
	
	@Override
	public boolean enterFluentAssignment(FluentAssignment arg) {
		return true;
	}
	
	@Override
	public boolean enterForAllStatement(ForAllStatement arg) {
		return true;
	}
	
	@Override
	public boolean enterWhenStatement(WhenStatement arg) {
		return true;
	}
	
	@Override
	public void leaveAndStatement(AndStatement arg) {
		// nothing to do here
	}

	@Override
	public void leaveBooleanAssignment(BooleanAssignment arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveEmptyStatement(EmptyStatement arg) {
		// nothing to do here
	}

	@Override
	public void leaveFluentAssignment(FluentAssignment arg) {
		// nothing to do here
	}

	@Override
	public void leaveForAllStatement(ForAllStatement arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveWhenStatement(WhenStatement arg) {
		// nothing to do here
	}
}
