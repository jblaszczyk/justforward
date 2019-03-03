package just.forward.api.statement;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;

import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Proposition;

@Immutable
public final class BooleanAssignment extends Assignment {

	private final AtomLiteral atom;
	
	private BooleanAssignment(AtomLiteral atom) {
		Objects.requireNonNull(atom);
		this.atom = atom;
	}
	
	public static BooleanAssignment of(AtomLiteral atom) {
		return new BooleanAssignment(atom);
	}

	public static BooleanAssignment negationOf(AtomLiteral atom) {
		return new BooleanAssignment(atom.toNegated());
	}
	
	@Override
	public Statement substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}

		if( substitution.contains(atom) ) {
			Proposition newAtom = atom.substitute(substitution);
			if( newAtom instanceof AtomLiteral ) {
				return BooleanAssignment.of((AtomLiteral) atom.substitute(substitution));
			}
		}
		
		return this;
	}
	
	public AtomLiteral getAtom() {
		return atom;
	}
	
/*	@Override
	public BooleanAssignment toNegated() {
		return new BooleanAssignment(atom.toNegated());
	}
*/
	@Override
	public Set<Variable> getFreeVariables() {
		return atom.getFreeVariables();
	}

	@Override
	public Stream<Variable> freeVariables() {
		return atom.freeVariables();
	}

	@Override
	public boolean isGround() {
		return atom.isGround();
	}
	
	@Override
	public BooleanAssignment ground(VariableSubstitution substitution) {
		return new BooleanAssignment(atom.ground(substitution));
	}

	@Override
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return atom.contains(clazz);
	}

	@Override
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}
	
	@Override
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterBooleanAssignment(this) ) {
			visitor.leaveBooleanAssignment(this);
		}
	}
		@Override
	public String toString() {
		return ":=(" + atom.getPredicate().getName().toString() + '(' +  Joiner.on(',').join(atom.getArguments()) + ")," + ((atom.isNegated())?("false"):("true")) + ")";
//		return atom.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO AtomLiteral in StructuralComparator
		BooleanAssignment other = (BooleanAssignment) obj;
		return Objects.equals(this.atom, other.atom);
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				atom
		);
	}
}
