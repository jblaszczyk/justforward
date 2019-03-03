package just.forward.api.proposition;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.Parameters;
import just.forward.api.common.Predicate;
import just.forward.api.expression.Variable;

@Immutable
public class Rule {
	
	private final boolean isSynthetic;
	private final Predicate predicate;
	private final Proposition body;
	
	private Rule(boolean isSynthetic, Predicate predicate, Proposition body) {
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(body);
		this.isSynthetic = isSynthetic;
		this.predicate = predicate;
		this.body = body;

		if( !ImmutableSet.copyOf(predicate.getParameters()).equals( body.getFreeVariables() ) ) {
			throw new IllegalArgumentException("DerivedPredicate's parameters don't match its body's free variables");
		}
	}
	
	public static Rule newInstance(Predicate predicate, Proposition body) {
		return new Rule(false, predicate, body);
	}
	
	public static Rule newSyntheticInstance(Predicate predicate, Proposition body) {
		return new Rule(true, predicate, body);
	}
	
	public boolean isSynthetic() {
		return isSynthetic;
	}
	
	public Predicate getPredicate() {
		return predicate;
	}
	
	public Stream<Variable> parameters() {
		return predicate.parameters();
	}	
	
	public List<Variable> getParameters() {
		return predicate.getParameters();
	}
	
	public Parameters getParametersStruct() {
		return predicate.getParametersStruct();
	}
	
	public Proposition getBody() {
		return body;
	}

	public Set<Variable> getAllVariables() {
		return allVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> allVariables() {
		return Stream.concat(freeVariables(), quantifiedVariables());
	}
	public Set<Variable> getQuantifiedVariables() {
		return quantifiedVariables().collect(Collectors.toSet());
	}	
	public Stream<Variable> quantifiedVariables() {
		return Stream.concat(universallyQuantifiedVariables(), existentiallyQuantifiedVariables());
	}
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> freeVariables() {
		return body.freeVariables();
	}
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> universallyQuantifiedVariables() {
		return body.universallyQuantifiedVariables();
	}
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return body.existentiallyQuantifiedVariables();
	}
	
	@Override
	public String toString() {
		return predicate.toString() + " :- (" + body.toString()+ ')';
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Rule other = (Rule) obj;
		return Objects.equals(this.predicate, other.predicate) &&
				Objects.equals(this.body, other.body);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				predicate,
				body
		);
	}

}
