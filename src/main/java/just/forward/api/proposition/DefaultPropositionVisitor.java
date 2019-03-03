package just.forward.api.proposition;

public class DefaultPropositionVisitor implements PropositionVisitor {

	@Override
	public boolean enterAndProposition(AndProposition arg) {
		return true;
	}

	@Override
	public boolean enterAtomLiteral(AtomLiteral arg) {
		return true;
	}

	@Override
	public boolean enterBooleanLiteral(BooleanLiteral arg) {
		return true;
	}
	
	@Override
	public boolean enterEmptyProposition(EmptyProposition arg) {
		return true;
	}
	
	@Override
	public boolean enterExistsProposition(ExistsProposition arg) {
		return true;
	}
	
	@Override
	public boolean enterForAllProposition(ForAllProposition arg) {
		return true;
	}
	
	@Override
	public boolean enterNotProposition(NotProposition arg) {
		return true;
	}
	
	@Override
	public boolean enterOrProposition(OrProposition arg) {
		return true;
	}
	
	@Override
	public void leaveAndProposition(AndProposition arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveAtomLiteral(AtomLiteral arg) {
		// nothing to do here
	}

	@Override
	public void leaveBooleanLiteral(BooleanLiteral arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveEmptyProposition(EmptyProposition arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveExistsProposition(ExistsProposition arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveForAllProposition(ForAllProposition arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveNotProposition(NotProposition arg) {
		// nothing to do here
	}
	
	@Override
	public void leaveOrProposition(OrProposition arg) {
		// nothing to do here
	}
}
