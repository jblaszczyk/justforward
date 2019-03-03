package just.forward.api.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import just.forward.api.common.Substitution;
import just.forward.api.proposition.AtomLiteral;

@Immutable
public class Tuple {

	private final ImmutableList<Term> terms;
	
	private Tuple(List<? extends Term> terms) {
		Objects.requireNonNull(terms);
		this.terms = ImmutableList.copyOf(terms);
	}
	
	public static Tuple newInstance(List<? extends Term> terms) {
		return new Tuple(terms);
	}
	
	public static Tuple newInstance(Stream<? extends Term> terms) {
		return newInstance(terms.collect(Collectors.toList()));
	}
	
	public static Tuple newInstance(Term... terms) {
		return newInstance(Arrays.asList(terms));
	}
	
	public List<Term> getTerms() {
		return terms;
	}
	
	public Stream<Term> terms() {
		return terms.stream();
	}
	
	public Term getTerm(int i) {
		return terms.get(i);
	}
	
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}
	
	public Stream<Variable> freeVariables() {
		return terms()
			.flatMap(Term::freeVariables);
	}
	
	public boolean isGround() {
		return terms().allMatch(Term::isGround);
	}
	
	public Tuple ground(VariableSubstitution substitution) {
		return new Tuple(
			terms()
				.map(e -> e.ground(substitution))
				.collect(Collectors.toList())
		);
	}
	
	public Tuple substitute(Substitution substitution) {
		return new Tuple(
			terms()
				.map(e -> e.substitute(substitution))
				.collect(Collectors.toList())
		);
	}
	
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return terms()
			.anyMatch(e -> e.uses(clazz));
	}
	
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}

//	@Override
	public void accept(TermVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterTuple(this) ) {
			terms.forEach(e -> e.accept(visitor));
			visitor.leaveTuple(this);
		}
	}
	
	@Override
	public String toString() {
		return '(' +  Joiner.on(',').join(terms) + ')';
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Tuple other = (Tuple) obj;
		
		return Objects.equals(this.terms, other.terms);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				terms
		);
	}
}
