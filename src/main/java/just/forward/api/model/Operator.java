package just.forward.api.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.Parameters;
import just.forward.api.common.QualifiedName;
import just.forward.api.common.Sentences;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.Statement;

@Immutable
public class Operator extends ProblemElement {

//	private final ImmutableList<Variable> parameters;
	private final Parameters parameters;
	private final Statement effect;
	
	@Nullable
	private final QualifiedName name;
	
	@Nullable
	private final Proposition precondition;
	

	private Operator(@Nullable QualifiedName name, @Nullable Proposition precondition, Statement effect, Parameters parameters ) {
//      Objects.requireNonNull(name);
//      Objects.requireNonNull(precondition);
        Objects.requireNonNull(effect);
        Objects.requireNonNull(parameters);
		this.name = name;
		this.precondition = precondition;
		this.effect = effect;
		this.parameters = parameters;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public Proposition getPrecondition() {
		return precondition;
	}

	public Statement getEffect() {
		return effect;
	}

	public Stream<Variable> parameters() {
		return parameters.variables();
	}
	
	public List<Variable> getParameters() {
		return parameters.getVariables();
	}
	
	public Parameters getParametersStruct() {
		return parameters;
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
		return Stream.concat(
			precondition.freeVariables(),
			effect.freeVariables()
		);
	}
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> universallyQuantifiedVariables() {
		return Stream.concat(
			precondition.universallyQuantifiedVariables(),
			effect.universallyQuantifiedVariables()
		);
	}
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return Stream.concat(
			precondition.existentiallyQuantifiedVariables(),
			effect.existentiallyQuantifiedVariables()
		);
	}
	
	@Override
	public void accept(ModelVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterOperator(this) ) {
			visitor.leaveOperator(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				parameters,
				precondition,
				effect
		);
	}

	@Override
	public String toString() {
		return name.toString() + parameters.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Operator other = (Operator) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.parameters, other.parameters) &&
				Objects.equals(this.precondition, other.precondition) &&
				Objects.equals(this.effect, other.effect);
	}
	
	public static class Builder {
		
		QualifiedName name;
		List<Variable> parameters = Lists.newArrayList();
		Proposition precondition = Sentences.trueValue();
		Statement effect;

		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}

		public Builder precondition(Proposition precondition) {
			Objects.requireNonNull(precondition);
//			if( precondition.uses(WhenSentence.class) ) {
//				throw new IllegalArgumentException("Operator precondition cannot contain WhenSentence");
//			}
			
			this.precondition = precondition;
			return this;
		}

		public Builder effect(Statement effect) {
			Objects.requireNonNull(effect);
//			if( effect.uses(ExistsSentence.class) ) {
//				throw new IllegalArgumentException("Operator effect cannot contain ExistsSentence");
//			}
			
			// TODO: cannot use DerivedPredicate in effects
			
			this.effect = effect;
			return this;
		}
		
		public Builder parameter(Variable parameter) {
			Objects.requireNonNull(parameter);
			parameters.add(parameter);
			return this;
		}
		
		public Builder parameters(Iterable<Variable> parameters) {
			Objects.requireNonNull(parameters);
			Iterables.addAll( this.parameters, parameters );
			return this;
		}
		
		public Builder parameter(Stream<Variable> parameters) {
			Objects.requireNonNull(parameters);
			this.parameters.addAll( parameters.collect( Collectors.toList() ));
			return this;
		}

		public Builder parameters(Variable... parameters) {
			Objects.requireNonNull(parameters);
			Iterables.addAll( this.parameters, Arrays.asList(parameters) );
			return this;
		}		

		public Builder parameters(Parameters parameters) {
			Objects.requireNonNull(parameters);
			this.parameters.addAll(parameters.getVariables());
			return this;
		}		

		public Operator build() {
			if( effect == null ) {
				throw new IllegalArgumentException("Must provide effects");
			}
			
			return new Operator(name, precondition, effect, Parameters.newInstance(parameters));
		}

	}
}
