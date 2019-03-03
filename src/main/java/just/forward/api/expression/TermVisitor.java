package just.forward.api.expression;

public interface TermVisitor {

	boolean enterVariable(Variable variable);
	void leaveVariable(Variable variable);

	boolean enterConstant(Constant constant);
	void leaveConstant(Constant constant);

	boolean enterFluentTerm(FluentTerm fluentTerm);
	void leaveFluentTerm(FluentTerm fluentTerm);
	
	boolean enterTuple(Tuple tuple);
	void leaveTuple(Tuple tuple);
}

