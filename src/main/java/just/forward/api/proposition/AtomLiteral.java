package just.forward.api.proposition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import just.forward.api.common.Predicate;
import just.forward.api.common.Substitution;
import just.forward.api.expression.Term;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Tuple;
import just.forward.api.expression.Variable;

@Immutable
public final class AtomLiteral extends Literal {

	private final Predicate predicate;
//	private final ImmutableList<Term> arguments;
	private final Tuple arguments;

	private AtomLiteral(Predicate predicate, Tuple arguments, boolean isNegated) {
		super(isNegated);
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(arguments);
		this.predicate = predicate;
		this.arguments = arguments;
	}

	public static AtomLiteral newInstance(Predicate predicate, Tuple arguments) {
		return new AtomLiteral(predicate, arguments, false);
	}
	public static AtomLiteral newInstance(Predicate predicate, List<? extends Term> arguments) {
		return newInstance(predicate, Tuple.newInstance(arguments));
	}
	public static AtomLiteral newInstance(Predicate predicate, Stream<? extends Term> arguments) {
		return newInstance(predicate, Tuple.newInstance(arguments) );
	}
	public static AtomLiteral newInstance(Predicate predicate, Term... arguments) {
		return newInstance(predicate, Tuple.newInstance(arguments) );
	}
	
	@Override
	public Proposition substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}

		if( substitution.contains(this.toNegated()) ) {
			return substitution.get(this.toNegated()).toNegated();
		}
/*		
		return AtomLiteral.newInstance(
			predicate, 
			arguments().map(e -> e.substitute(substitution))
		);
*/
		return AtomLiteral.newInstance(predicate, arguments.substitute(substitution));
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public List<Term> getArguments() {
		return arguments.getTerms();
	}
	public Stream<Term> arguments() {
		return arguments.terms();
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
	public boolean isGround() {
		return arguments.isGround();
	}
	
	@Override
	public AtomLiteral ground(VariableSubstitution substitution) {
		return new AtomLiteral(predicate, arguments.ground(substitution), isNegated());
	}
	
	@Override
	public AtomLiteral toNegated() {
		return new AtomLiteral(predicate, arguments, !isNegated());
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void accept(PropositionVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterAtomLiteral(this) ) {
			visitor.leaveAtomLiteral(this);
		}
	}

	@Override
	public String toString() {
		return ((isNegated())?("!"):("")) + predicate.getName().toString() + arguments.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		AtomLiteral other = (AtomLiteral) obj;
		
		return Objects.equals(this.predicate, other.predicate) &&
				Objects.equals(this.arguments, other.arguments) &&
				Objects.equals(this.isNegated(), other.isNegated());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				predicate,
				arguments,
				isNegated()
		);
	}
}
