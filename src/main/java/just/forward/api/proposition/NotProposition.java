package just.forward.api.proposition;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;

@Immutable
public final class NotProposition extends AbstractCompoundProposition {

	private NotProposition(Proposition proposition) {
		super(ImmutableList.of(proposition));
	}
	
	public static NotProposition of(Proposition proposition) {
		return new NotProposition(proposition);
	}
	
	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return NotProposition.of(getChild()).substitute(substitution);
	}

	public Proposition getChild() {
		return getChildren().iterator().next();
	}
	
	@Override
	public NotProposition ground(VariableSubstitution subsitution) {
		return new NotProposition(getChild().ground(subsitution));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return getChild().getFreeVariables();
	}	
	@Override
	public Stream<Variable> freeVariables() {
		return getChild().freeVariables();
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return getChild().getUniversallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return getChild().universallyQuantifiedVariables();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return getChild().getExistentiallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return getChild().existentiallyQuantifiedVariables();
	}

	@Override
	public boolean isNNF() {
		return false;
	}

	@Override
	public boolean isPNF() {
		return !contains(QuantifiedProposition.class);
	}

	@Override
	public boolean isDNF() {
		return false; // DNF should have only (negated) literals
	}

	@Override
	public Proposition toNNF() {
		return getChild().toNegated().toNNF();
	}
	
	@Override
	public Proposition toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		return toNNF().toPNF();
	}

	@Override
	public Proposition toDNF() {
		return toNNF().toDNF();
	}

	@Override
	public Proposition removeQuantifiers() {
		if( !contains(QuantifiedProposition.class) ) {
			return this;
		}
		
		return NotProposition.of( getChild().removeQuantifiers() );
	}

	@Override
	public Proposition toNegated() {
		return getChild();
	}		
	
	@Override
	public Proposition toSimplified() {
		return toNNF().toSimplified();
	}
	
	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterNotProposition(this) ) {
			getChild().accept(visitor);
			visitor.leaveNotProposition(this);
		}
	}

	@Override
	public String toString() {
		return "not(" + getChild().toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO NotStatement in StructuralComparator
		NotProposition other = (NotProposition) obj;
		return Objects.equals(this.getChildren(), other.getChildren());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			getClass().getName(),
			getChildren()
		);
	}	
	
}
