package just.forward.normalization.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.Parameters;
import just.forward.api.common.Predicate;
import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Literal;
import just.forward.api.proposition.Rule;

@Immutable
public class NormalizedRule {

	private static final java.util.function.Predicate<Literal> FILTER_POSITIVE = e -> !e.isNegated();
	private static final java.util.function.Predicate<Literal> FILTER_NEGATIVE = e -> e.isNegated();

	private final boolean isSynthetic; // TODO needed?
	private final Predicate predicate;
	private final ImmutableList<Literal> body;
	private final Rule originalRule;
	
	public NormalizedRule(boolean isSynthetic, @Nullable Rule originalRule, Predicate predicate, Iterable<Literal> body) {
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(body);
		this.isSynthetic = isSynthetic;
		this.originalRule = originalRule;
		this.body = ImmutableList.copyOf(body);
		this.predicate = predicate;
	}

	public static Builder builder(Predicate predicate) {
		return new Builder(predicate);
	}
	
	public boolean isSynthetic() {
		return isSynthetic;
	}
	
	public QualifiedName getName() {
		return predicate.getName();
	}
	
	public Rule getOriginal() {
		return originalRule;
	}
	
	public Predicate getPredicate() {
		return predicate;
	}
	
	public Parameters getParametersStruct() {
		return predicate.getParametersStruct();
	}
	
	public List<Literal> getPositiveBody() {
		return positiveBody().collect(Collectors.toList());
	}
	
	public List<Literal> getNegativeBody() {
		return negativeBody().collect(Collectors.toList());
	}
	
	public List<Literal> getBody() {
		return body;
	}

	public Stream<Literal> positiveBody() {
		return body.stream()
			.filter(FILTER_POSITIVE);
	}
	public Stream<Literal> negativeBody() {
		return body.stream()
			.filter(FILTER_NEGATIVE);
	}
	
	public Stream<Literal> body() {
		return body.stream();
	}

	@Override
	public String toString() {
		return predicate.toString() + " :- " + Joiner.on(',').join(getBody());
	}
	
	Stream<Predicate> predicatesInBody() {
		return body()
			.filter( e -> e instanceof AtomLiteral)
			.map( e -> ((AtomLiteral) e).getPredicate() );
	}
	
	public List<Variable> getParameters() {
		return predicate.getParameters();
	}
	
	public Stream<Variable> parameters() {
		return getPredicate().parameters();
	}
	
	public GroundNormalizedRule ground(VariableSubstitution substitution) {
		return GroundNormalizedRule.newInstance(
			this, 
			body()
				.map(e -> (Literal) e.ground(substitution))
		);
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
		return body().flatMap(e -> e.freeVariables());
	}
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> universallyQuantifiedVariables() {
		return body().flatMap(e -> e.universallyQuantifiedVariables());
	}
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return body().flatMap(e -> e.existentiallyQuantifiedVariables());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
				predicate,
				body
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		NormalizedRule other = (NormalizedRule) obj;
		return Objects.equals(this.predicate, other.predicate) &&
				Objects.equals(this.body, other.body);
	}
	
	public static class Builder {

		
		boolean isSynthetic;
		Predicate predicate;
		List<Literal> body = Lists.newArrayList();
		Rule originalRule;

		Builder(Predicate predicate) {
			Objects.requireNonNull(predicate);
			this.predicate = predicate;
		}

		public Builder synthetic() {
			isSynthetic = true;
			return this;
		}
		
		public Builder synthetic(boolean isSynthetic) {
			this.isSynthetic = isSynthetic;
			return this;
		}
		
		public Builder original(Rule original) {
			Objects.requireNonNull(original);
			this.originalRule = original;
			return this;
		}

		public Builder atom(Literal atom) {
			Objects.requireNonNull(atom);
			body.add(atom);
			return this;
		}
		
		public Builder atoms(Iterable<Literal> atoms) {
			Objects.requireNonNull(atoms);
			Iterables.addAll(this.body, atoms);
			return this;
		}
		
		public Builder atoms(Stream<Literal> atoms) {
			Objects.requireNonNull(atoms);
			return atoms(atoms.collect(Collectors.toList()));
		}
		
		public Builder atoms(Literal... effects) {
			Objects.requireNonNull(effects);
			return atoms(Arrays.asList(effects));
		}		
		
		public NormalizedRule build() {
			return new NormalizedRule(isSynthetic, originalRule, predicate, body);
		}
	}
}
