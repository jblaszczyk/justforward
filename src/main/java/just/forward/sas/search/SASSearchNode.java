package just.forward.sas.search;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import just.forward.api.proposition.Literal;
import just.forward.sas.model.AbstractSASPartialAssignment;
import just.forward.sas.model.SASAction;
import just.forward.sas.model.SASConditionalAssignment;
import just.forward.sas.model.SASPartialAssignmentMutable;
import just.forward.sas.model.SASVariable;

public class SASSearchNode {

	SASSearchNode parent;
	SASAction action;
	SASPartialAssignmentMutable state;
	
	SASSearchNode(AbstractSASPartialAssignment state) {
		Objects.requireNonNull(state);
		this.state = SASPartialAssignmentMutable.copyOf(state);
	}
	
	protected SASSearchNode(SASSearchNode parent, SASAction action) {
		Objects.requireNonNull(parent);
		Objects.requireNonNull(action);
		this.parent = parent;
		this.action = action;
	}
	
	public static SASSearchNode of(AbstractSASPartialAssignment state) {
		return new SASSearchNode(state);
	}
	
	public boolean isApplicable(SASAction action) {
		return state.entailsAll(action.getPreconditions());
	}

	public SASSearchNode apply(SASAction action) {
		SASSearchNode newNode = new SASSearchNode(this, action);
		newNode.state = state.duplicate();
		
		for( SASConditionalAssignment conditional : action.getEffects() ) {
			if( state.entailsAll(conditional.getCondition())) {
				newNode.state = newNode.state.update( conditional.getAssignment() );
			}
		}
		
		return newNode;
	}

	public boolean entails(SASVariable var, Literal value) {
		return state.entails(var, value);
	}

	public boolean entailsAll(AbstractSASPartialAssignment otherSubstitution) {
		return state.entailsAll(otherSubstitution);
	}

	public List<SASAction> getPath() {
		if( parent == null ) {
			return Lists.newLinkedList();
		}
		
		List<SASAction> result = parent.getPath();
		result.add(action);
		
		return result;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				parent, // NOTE: this recursion may be slow
				action,
				state
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
		
		SASSearchNode other = (SASSearchNode) obj;
		return Objects.equals(this.parent, other.parent) && // NOTE: this recursion may be slow
				Objects.equals(this.action, other.action) &&
				Objects.equals(this.state, other.state);
	}
	
}
