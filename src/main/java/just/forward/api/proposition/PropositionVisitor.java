package just.forward.api.proposition;

public interface PropositionVisitor {

	boolean enterAndProposition(AndProposition arg);
	void leaveAndProposition(AndProposition arg);
	
	boolean enterAtomLiteral(AtomLiteral arg);
	void leaveAtomLiteral(AtomLiteral arg);
	
	boolean enterBooleanLiteral(BooleanLiteral arg);
	void leaveBooleanLiteral(BooleanLiteral arg);
	
	boolean enterEmptyProposition(EmptyProposition arg);
	void leaveEmptyProposition(EmptyProposition arg);
	
	boolean enterExistsProposition(ExistsProposition arg);
	void leaveExistsProposition(ExistsProposition arg);
	
	boolean enterForAllProposition(ForAllProposition arg);
	void leaveForAllProposition(ForAllProposition arg);
	
	boolean enterNotProposition(NotProposition arg);
	void leaveNotProposition(NotProposition arg);
	
	boolean enterOrProposition(OrProposition arg);
	void leaveOrProposition(OrProposition arg);
}
