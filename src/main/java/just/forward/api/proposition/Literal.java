package just.forward.api.proposition;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;

public abstract class Literal extends Proposition {

	private boolean isNegated;
	
	protected Literal(boolean isNegated) {
		this.isNegated = isNegated;
	}
	
	public boolean isNegated() {
		return isNegated;
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
	public Literal toNNF() {
		return this;
	}

	@Override
	public Literal toPNF() {
		return this;
	}

	@Override
	public Literal toDNF() {
		return this;
	}
	
	@Override
	public abstract Literal ground(VariableSubstitution substitution);
	
	@Override
	public abstract Literal toNegated();

	@Override
	public Literal removeQuantifiers() {
		return this;
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
		return getFreeVariables();
	}
	@Override
	public Stream<Variable> allVariables() {
		return freeVariables();
	}
	
	@Override
	public Proposition toSimplified() {
		if( isEmpty() ) {
			return EmptyProposition.getInstance();
		}
		
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
}
