package just.forward.api.proposition;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;

@Immutable
public final class BooleanLiteral extends Literal {

	private final static BooleanLiteral TRUE = new BooleanLiteral(false);
	private final static BooleanLiteral FALSE = new BooleanLiteral(true);

	private static final Predicate<Proposition> FILTER_TRUE = e -> getTrue().equals(e);
	private static final Predicate<Proposition> FILTER_FALSE = e -> getFalse().equals(e);
	private static final Predicate<Proposition> FILTER_OUT_TRUE = e -> !getTrue().equals(e);
	private static final Predicate<Proposition> FILTER_OUT_FALSE = e -> !getFalse().equals(e);

	public static Predicate<Proposition> filterTrue() {
		return FILTER_TRUE;
	}

	public static Predicate<Proposition> filterFalse() {
		return FILTER_FALSE;
	}

	public static Predicate<Proposition> filterOutTrue() {
		return FILTER_OUT_TRUE;
	}

	public static Predicate<Proposition> filterOutFalse() {
		return FILTER_OUT_FALSE;
	}

	private BooleanLiteral(boolean isNegated) {
		super(isNegated);
	}
	
	public static BooleanLiteral getTrue() {
		return TRUE;
	}
	public static BooleanLiteral getFalse() {
		return FALSE;
	}

	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return this;
	}

	@Override
	public Literal toNegated() {
		return isNegated() ? getTrue() : getFalse();
	}
	
	@Override
	public boolean isGround() {
		return true;
	}
	
	@Override
	public BooleanLiteral ground(VariableSubstitution substitution) {
		return this;
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return ImmutableSet.of();
	}

	@Override
	public Stream<Variable> freeVariables() {
		return Stream.of();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterBooleanLiteral(this) ) {
			visitor.leaveBooleanLiteral(this);
		}
	}

	@Override
	public String toString() {
		return isNegated() ? "false" : "true";
	}
}