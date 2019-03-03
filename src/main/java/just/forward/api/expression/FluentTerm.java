package just.forward.api.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import just.forward.api.common.Fluent;
import just.forward.api.common.Substitution;

public class FluentTerm extends Term {

	private final Fluent fluent;
//	private final ImmutableList<Term> arguments;
	private final Tuple arguments;

	private FluentTerm(Fluent fluent, Tuple arguments) {
		Objects.requireNonNull(fluent);
		Objects.requireNonNull(arguments);
		this.fluent = fluent;
		this.arguments = arguments;
	}

	public static FluentTerm newInstance(Fluent fluent, Tuple arguments) {
		return new FluentTerm(fluent, arguments);
	}

	public static FluentTerm newInstance(Fluent fluent, List<? extends Term> arguments) {
		return newInstance(fluent, Tuple.newInstance(arguments) );
	}
	public static FluentTerm newInstance(Fluent fluent, Stream<? extends Term> arguments) {
		return newInstance(fluent, Tuple.newInstance(arguments) );
	}
	public static FluentTerm newInstance(Fluent fluent, Term... arguments) {
		return newInstance(fluent, Tuple.newInstance(arguments) );
	}

	@Override
	public Term substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}

		return FluentTerm.newInstance(fluent, arguments.substitute(substitution));
	}

	public Fluent getFluent() {
		return fluent;
	}

	public Tuple getArgumentsTuple() {
		return arguments;
	}
	
	public List<Term> getArguments() {
		return arguments.getTerms();
	}

	public Stream<Term> arguments() {
		return arguments.terms();
	}

	@Override
	public boolean isGround() {
		return arguments().anyMatch(Term::isGround);
	}

	@Override
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return arguments.contains(clazz);
	}

	@Override
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}
	
	public FluentTerm ground(VariableSubstitution substitution) {
		return FluentTerm.newInstance(fluent, arguments.ground(substitution));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return arguments.getFreeVariables();
	}
	@Override
	public Stream<Variable> freeVariables() {
		return arguments.freeVariables();
	}

	@Override
	public void accept(TermVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterFluentTerm(this) ) {
			arguments.accept(visitor);
			visitor.leaveFluentTerm(this);
		}
	}

	@Override
	public String toString() {
		return fluent.getName().toString() + arguments.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		FluentTerm other = (FluentTerm) obj;
		return Objects.equals(this.fluent, other.fluent) &&
				Objects.equals(this.arguments, other.arguments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass(),
				fluent,
				arguments
		);
	}
}
