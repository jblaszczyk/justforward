package just.forward.api.model;

public interface ModelVisitor {

	boolean enterDomain(Domain domain);
	void leaveDomain(Domain domain);
	
	boolean enterProblem(Problem problem);
	void leaveProblem(Problem problem);
	
	boolean enterOperator(Operator operator);
	void leaveOperator(Operator operator);
}
