package just.forward.api.expression;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.QualifiedName;
import just.forward.api.common.Substitution;

@Immutable
public class Constant extends Term {

	QualifiedName name;
	
	private Constant(QualifiedName name) {
		Objects.requireNonNull(name);
		this.name = name;
	}
	
	public static Constant newInstance(QualifiedName name) {
		return new Constant(name);
	}
	public static Constant newInstance(String name) {
		return new Constant(QualifiedName.of(name));
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
		if( visitor.enterConstant(this) ) {
			visitor.leaveConstant(this);
		}
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
	public boolean isGround() {
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
	public Term ground(VariableSubstitution substitution) {
		return this;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Constant other = (Constant) obj;
		return Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass(),
				name
		);
	}
}
