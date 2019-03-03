package just.forward.api.statement;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import just.forward.api.common.Fluent;
import just.forward.api.common.Substitution;
import just.forward.api.expression.FluentTerm;
import just.forward.api.expression.Term;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;

@Immutable
public final class FluentAssignment extends Assignment {

	private final FluentTerm fluentExpression;
	private final Term expression;
	
	private FluentAssignment(FluentTerm fluentExpression, Term expression) {
		Objects.requireNonNull(fluentExpression);
		Objects.requireNonNull(expression);
		this.fluentExpression = fluentExpression;
		this.expression = expression;
	}
	
	public static FluentAssignment newInstance(FluentTerm fluentExpression, Term expression) {
		return new FluentAssignment(fluentExpression, expression);
	}
	
	@Override
	public Statement substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}

		Term newFluentExpression = fluentExpression.substitute(substitution);
		if( newFluentExpression instanceof FluentTerm ) {
			return FluentAssignment.newInstance((FluentTerm) newFluentExpression, expression.substitute(substitution));
		}
		
		return this;
	}
	
	public FluentTerm getFluentExpression() {
		return fluentExpression;
	}
	
	public Term getExpression() {
		return expression;
	}
	
	public Fluent getFluent() {
		return fluentExpression.getFluent();
	}

	public List<Term> getArguments() {
		return fluentExpression.getArguments();
	}
	public Stream<Term> arguments() {
		return fluentExpression.arguments();
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}

	@Override
	public Stream<Variable> freeVariables() {
		return Stream.concat(fluentExpression.freeVariables(), expression.freeVariables());
	}

	@Override
	public boolean isGround() {
		return fluentExpression.isGround() && expression.isGround();
		 
	}

	@Override
	public FluentAssignment ground(VariableSubstitution substitution) {
		return new FluentAssignment(fluentExpression.ground(substitution), expression.ground(substitution));
	}
	
/*	@Override
	public Statement toNegated() {
		throw new AssertionError();
	}
*/
	@Override
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return fluentExpression.contains(clazz) || expression.contains(clazz);
	}

	@Override
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}
	
	@Override
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterFluentAssignment(this) ) {
			visitor.leaveFluentAssignment(this);
		}
	}
	
	@Override
	public String toString() {
		return ":=(" + fluentExpression.toString() + "," + expression.toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		FluentAssignment other = (FluentAssignment) obj;
		return Objects.equals(this.fluentExpression, other.fluentExpression) && 
				Objects.equals(this.expression, other.expression);
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				fluentExpression,
				expression
		);
	}
}
