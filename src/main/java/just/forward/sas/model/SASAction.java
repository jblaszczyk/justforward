package just.forward.sas.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.QualifiedName;

@Immutable
public class SASAction {

	SASPartialAssignmentImmutable preconditions;
	ImmutableSet<SASConditionalAssignment> effects;
//	ImmutableList<Variable> parameters; TODO

	QualifiedName name;
	
	SASAction(QualifiedName name, AbstractSASPartialAssignment preconditions, Iterable<SASConditionalAssignment> effects) {
		Objects.requireNonNull(preconditions);
		Objects.requireNonNull(effects);
		Objects.requireNonNull(name);
		this.name = name;
		this.preconditions = SASPartialAssignmentImmutable.copyOf(preconditions);
		this.effects = ImmutableSet.copyOf(effects);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public AbstractSASPartialAssignment getPreconditions() {
		return preconditions;
	}
	
	public Set<SASConditionalAssignment> getEffects() {
		return effects;
	}
	
	public QualifiedName getName() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				preconditions,
				effects
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
		
		SASAction other = (SASAction) obj;
		return 	Objects.equals(this.preconditions, other.preconditions) &&
				Objects.equals(this.effects, other.effects);
	}
	
	@Override
	public String toString() {
//		return name.toString() + '(' +  Joiner.on(',').join(parameters) + ')'; TODO
		return name.toString();
	}

	public static class Builder {
		
		SASPartialAssignmentMutable preconditions;
		List<SASConditionalAssignment> effects = Lists.newLinkedList();
		
		QualifiedName name;

		Builder() {
		}
		
		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}
		
		public Builder preconditions(AbstractSASPartialAssignment partialAssignment) {
			Objects.requireNonNull(partialAssignment);
			if( this.preconditions == null ) {
				this.preconditions = SASPartialAssignmentMutable.copyOf(partialAssignment);
			} else {
				this.preconditions.merge(partialAssignment);
			}
			
			return this;
		}
		
		public Builder effect(SASConditionalAssignment effect) {
			Objects.requireNonNull(effect);
			effects.add(effect);
			return this;
		}
		
		public Builder effects(Iterable<SASConditionalAssignment> effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, effects );
			return this;
		}
		
		public Builder effects(Stream<SASConditionalAssignment> effects) {
			Objects.requireNonNull(effects);
			this.effects.addAll( effects.collect( Collectors.toList() ));
			return this;
		}

		public Builder effects(SASConditionalAssignment... effects) {
			Objects.requireNonNull(effects);
			Iterables.addAll( this.effects, Arrays.asList(effects) );
			return this;
		}
		
		public SASAction build() {
			return new SASAction(name, preconditions, effects);
		}
	}
	
}
