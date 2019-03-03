package just.forward.sas.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Constant;
import just.forward.normalization.model.GroundNormalizedTask;

public class SASTask {

	ImmutableSet<SASAction> actions;
	ImmutableSet<Constant> objects;
	
	SASPartialAssignmentImmutable init;
	SASPartialAssignmentImmutable goal;

	@Nullable
	QualifiedName name;
	
	@Nullable
	GroundNormalizedTask original;
	
	private SASTask(@Nullable QualifiedName name, @Nullable GroundNormalizedTask original, Iterable<SASAction> actions, Iterable<Constant> objects, AbstractSASPartialAssignment init, AbstractSASPartialAssignment goal) {
		Objects.requireNonNull(actions);
		Objects.requireNonNull(objects);
		Objects.requireNonNull(init);
		Objects.requireNonNull(goal);
		this.actions = ImmutableSet.copyOf(actions);
		this.objects = ImmutableSet.copyOf(objects);
		this.init = SASPartialAssignmentImmutable.copyOf(init);
		this.goal = SASPartialAssignmentImmutable.copyOf(goal);
		this.name = name;
		this.original = original;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public ImmutableSet<SASAction> getActions() {
		return actions;
	}
	
	public Stream<SASAction> actions() {
		return actions.stream();
	}
	
	public ImmutableSet<Constant> getObjects() {
		return objects;
	}
	
	public Stream<Constant> objects() {
		return objects.stream();
	}
	
	public SASPartialAssignmentImmutable getInit() {
		return init;
	}
	
	public SASPartialAssignmentImmutable getGoal() {
		return goal;
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public GroundNormalizedTask getOriginal() {
		return original;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				actions,
				objects,
				init,
				goal
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
		
		SASTask other = (SASTask) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.actions, other.actions) &&
				Objects.equals(this.objects, other.objects) &&
				Objects.equals(this.init, other.init) &&
				Objects.equals(this.goal, other.goal);
	}
	
	public static class Builder {

		Set<SASAction> actions = Sets.newHashSet();
		Set<Constant> objects = Sets.newHashSet();
		
		AbstractSASPartialAssignment init;
		AbstractSASPartialAssignment goal;

		QualifiedName name;
		GroundNormalizedTask original;

		Builder() {}
		
		@CanIgnoreReturnValue
		public Builder original(GroundNormalizedTask original) {
			Objects.requireNonNull(original);
			this.original = original;
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder name(String name) {
			Objects.requireNonNull(name);
			this.name = QualifiedName.of(name);
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder init(AbstractSASPartialAssignment init) {
			Objects.requireNonNull(init);
			this.init = init;
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder goal(AbstractSASPartialAssignment goal) {
			Objects.requireNonNull(goal);
			this.goal = goal;
			return this;
		}

		@CanIgnoreReturnValue
		public Builder action(SASAction action) {
			Objects.requireNonNull(action);
			actions.add(action);
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder actions(Iterable<SASAction> actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, actions );
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder actions(Stream<SASAction> actions) {
			Objects.requireNonNull(actions);
			this.actions.addAll( actions.collect( Collectors.toList() ));
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder actions(SASAction... actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, Arrays.asList(actions) );
			return this;
		}		
		
		@CanIgnoreReturnValue
		public Builder object(Constant object) {
			Objects.requireNonNull(object);
			objects.add(object);
			return this;
		}

		@CanIgnoreReturnValue
		public Builder object(String name) {
			return object(Constant.newInstance(name));
		}
		
		@CanIgnoreReturnValue
		public Builder objects(Iterable<Constant> objects) {
			Objects.requireNonNull(objects);
			Iterables.addAll( this.objects, objects );
			return this;
		}
		
		@CanIgnoreReturnValue
		public Builder objects(Stream<Constant> objects) {
			Objects.requireNonNull(objects);
			this.objects.addAll( objects.collect( Collectors.toList() ));
			return this;
		}

		@CanIgnoreReturnValue
		public Builder objects(Constant... objects) {
			Objects.requireNonNull(objects);
			Iterables.addAll( this.objects, Arrays.asList(objects) );
			return this;
		}
		public SASTask build() {
			
			if( init == null ) {
//				throw new IllegalArgumentException("Must provide init"); // needed?
			}
			
			if( goal == null || goal.isEmpty() ) {
				throw new IllegalArgumentException("Must provide goal");
			}

			if( actions.isEmpty() ) {
				throw new IllegalArgumentException("Domain should have at least one action");
			}


			return new SASTask(name, original, actions, objects, init, goal);
		}
	}

}
