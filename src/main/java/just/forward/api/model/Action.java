package just.forward.api.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import just.forward.api.expression.Term;
import just.forward.api.expression.Tuple;

public class Action {

	private final Operator operator;
	private final Tuple arguments;
	
	private Action(Operator operator, Tuple arguments) {
		Objects.requireNonNull(operator);
		Objects.requireNonNull(arguments);

		if( !arguments.isGround() ) {
			throw new IllegalArgumentException("Arguments of Action are not ground: " + arguments.toString());
		}

		this.operator = operator;
		this.arguments = arguments;
	}
	
	public static Action newInstance(Operator operator, Tuple arguments) {
		return new Action(operator, arguments);
	}
	
	public static Action newInstance(Operator operator, List<? extends Term> arguments) {
		return newInstance(operator, Tuple.newInstance(arguments));
	}
	
	public static Action newInstance(Operator operator, Stream<? extends Term> arguments) {
		return newInstance(operator, Tuple.newInstance(arguments));
	}

	public static Action newInstance(Operator operator, Term... arguments) {
		return newInstance(operator, Tuple.newInstance(arguments));
	}

	public Operator getOperator() {
		return operator;
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
	public String toString() {
		return operator.getName().toString() + arguments.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Action other = (Action) obj;
		return Objects.equals(this.operator, other.operator) &&
				Objects.equals(this.arguments, other.arguments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass(),
				operator,
				arguments
		);
	}
}
