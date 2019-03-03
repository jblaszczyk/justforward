package just.forward.api.expression;

import java.util.Set;
import java.util.stream.Stream;

import just.forward.api.common.Substitution;

public abstract class Term {

	public abstract Set<Variable> getFreeVariables();
	public abstract Stream<Variable> freeVariables();
	
	public abstract boolean isGround();
	public abstract Term ground(VariableSubstitution substitution);
	public abstract Term substitute(Substitution substitution);
	
	public abstract boolean contains(Class<?> clazz);
	public abstract boolean uses(Class<?> clazz);

	public abstract void accept(TermVisitor visitor);
}
