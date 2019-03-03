package just.forward.api.statement;

public interface StatementVisitor {
	
	boolean enterAndStatement(AndStatement arg);
	void leaveAndStatement(AndStatement arg);
	
	boolean enterBooleanAssignment(BooleanAssignment arg);
	void leaveBooleanAssignment(BooleanAssignment arg);
	
	boolean enterEmptyStatement(EmptyStatement arg);
	void leaveEmptyStatement(EmptyStatement arg);
	
	boolean enterFluentAssignment(FluentAssignment arg);
	void leaveFluentAssignment(FluentAssignment arg);
	
	boolean enterForAllStatement(ForAllStatement arg);
	void leaveForAllStatement(ForAllStatement arg);
	
	boolean enterWhenStatement(WhenStatement arg);
	void leaveWhenStatement(WhenStatement arg);
}
