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
import just.forward.api.statement.Statement;

@Immutable
public final class EmptyProposition extends Proposition {
	private static final EmptyProposition INSTANCE = new EmptyProposition();
	private static final Predicate<Proposition> FILTER_OUT = e -> !e.isEmpty();

	private EmptyProposition() {
	}
	
	public static EmptyProposition getInstance() {
		return INSTANCE;
	}
	
	public static Predicate<Proposition> filterOut() {
		return FILTER_OUT;
	}

	@Override
	public EmptyProposition substitute(Substitution substitution) {
		return this;
	}

	@Override
	public boolean isNNF() {
		return true;
	}

	@Override
	public boolean isPNF() {
		return true;
	}

	@Override
	public boolean isDNF() {
		return true;
	}

	@Override
	public EmptyProposition toNNF() {
		return this;
	}
	
	@Override
	public EmptyProposition toPNF() {
		return this;
	}

	@Override
	public EmptyProposition toDNF() {
		return this;
	}

	@Override
	public EmptyProposition removeQuantifiers() {
		return this;
	}

	@Override
	public EmptyProposition toNegated() {
		return this;
	}
	
	@Override
	public EmptyProposition toSimplified() {
		return this;
	}

	@Override
	public boolean isGround() {
		return true;
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
	public Set<Variable> getQuantifiedVariables() {
		return ImmutableSet.of();
	}	
	@Override
	public Stream<Variable> quantifiedVariables() {
		return Stream.of();
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return ImmutableSet.of();
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return Stream.of();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return ImmutableSet.of();
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return Stream.of();
	}
	@Override
	public Set<Variable> getAllVariables() {
		return ImmutableSet.of();
	}
	@Override
	public Stream<Variable> allVariables() {
		return Stream.of();
	}
	
	
	@Override
	public EmptyProposition ground(VariableSubstitution substitution) {
		return this;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return false;
	}

	@Override
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this);
	}

	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterEmptyProposition(this) ) {
			visitor.leaveEmptyProposition(this);
		}
	}

	@Override
	public String toString() {
		return "empty";
	}
}
