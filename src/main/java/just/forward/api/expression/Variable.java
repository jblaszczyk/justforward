package just.forward.api.expression;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.Sets;

import just.forward.api.common.QualifiedName;
import just.forward.api.common.Substitution;

@Immutable
public class Variable extends Term {

	QualifiedName name;
	
	private Variable(QualifiedName name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	public static Variable newInstance(QualifiedName name) {
		return new Variable(name);
	}
	public static Variable newInstance(String name) {
		return new Variable(QualifiedName.of(name));
	}

	@Override
	public Term substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}

		return this;
	}

	public QualifiedName getName() {
		return name;
	}
	
	@Override
	public void accept(TermVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterVariable(this) ) {
			visitor.leaveVariable(this);
		}
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return Sets.newHashSet(this);
	}

	@Override
	public Stream<Variable> freeVariables() {
		return Stream.of(this);
	}

	@Override
	public boolean isGround() {
		return false;
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
	public Term ground(VariableSubstitution substitution) {
		return substitution.findValue(this);
	}


	@Override
	public String toString() {
		return '$' + name.toString();
	}
}
