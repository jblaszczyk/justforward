package just.forward.api.statement;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.Literal;
import just.forward.api.proposition.Proposition;

@Immutable
public class EmptyStatement extends Statement {
	private static final EmptyStatement INSTANCE = new EmptyStatement();
	private static final Predicate<Statement> FILTER_OUT = e -> !e.isEmpty();

	private EmptyStatement() {}
	
	public static EmptyStatement getInstance() {
		return INSTANCE;
	}

	public static Predicate<Statement> filterOut() {
		return FILTER_OUT;
	}

	@Override
	public EmptyStatement substitute(Substitution substitution) {
		return this;
	}
/*
	@Override
	public EmptyStatement toNegated() {
		return this;
	}
	@Override
	public EmptyStatement simplify() {
		return this;
	}
 */	

	@Override
	public EmptyStatement flatten() {
		return this;
	}

	@Override
	public EmptyStatement reduce() {
		return this;
	}

	@Override
	public EmptyStatement emptify() {
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
	public EmptyStatement ground(VariableSubstitution substitution) {
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
	public boolean isWhenNF() {
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}
/*
	@Override
	public EmptyStatement toNNF() {
		return this;
	}
*/
	@Override
	public EmptyStatement toWhenNF() {
		return this;
	}
	
	@Override
	public EmptyStatement toPNF() {
		return this;
	}
	
	@Override
	public EmptyStatement toCNF() {
		return this;
	}
	
	@Override
	public EmptyStatement removeQuantifiers() {
		return this;
	}

	@Override
	protected EmptyStatement pullAndOverForAll() {
		return this;
	}
	
	@Override
	protected EmptyStatement pullAndOverWhen() {
		return this;
	}
	
	@Override
	protected EmptyStatement pushWhenOverForall() {
		return this;
	}
	
	@Override
	protected EmptyStatement pushWhenOverOr() {
		return this;
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
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterEmptyStatement(this) ) {
			visitor.leaveEmptyStatement(this);
		}
	}
	
	@Override
	public String toString() {
		return "empty";
	}
}
