package just.forward.api.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.QualifiedName;
import just.forward.api.common.Sentences;
import just.forward.api.expression.Constant;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.Statement;

@Immutable
public class Problem extends ProblemElement {

	@Nullable
	private final QualifiedName name;
	
	private final Domain domain;
	private final ImmutableSet<Constant> objects;
	private final Statement init;
	private final Proposition goal;
	
	private Problem(Domain domain, @Nullable QualifiedName name, Statement init, Proposition goal, Iterable<Constant> objects) {
		Objects.requireNonNull(domain);
		Objects.requireNonNull(name);
        Objects.requireNonNull(init);
        Objects.requireNonNull(goal);
        this.domain = domain;
		this.name = name;
		this.init = init;
		this.goal = goal;
		this.objects = ImmutableSet.copyOf(objects);
	}

	public static Builder builder(Domain domain) {
		return new Builder(domain);
	}
	
	public QualifiedName getName() {
		return name;
	}

	public Domain getDomain() {
		return domain;
	}
	
	public Statement getInit() {
		return init;
	}
	
	public Proposition getGoal() {
		return goal;
	}
	
	public Stream<Constant> objects() {
		return objects.stream();
	}
	
	public Stream<Constant> allObjects() {
		return Stream.concat( getDomain().constants(), objects() );
	}

	
	
	@Override
	public void accept(ModelVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterProblem(this) ) {
			domain.accept(visitor);
			visitor.leaveProblem(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				domain,
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
		
		Problem other = (Problem) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.domain, other.domain) &&
				Objects.equals(this.objects, other.objects) &&
				Objects.equals(this.init, other.init) &&
				Objects.equals(this.goal, other.goal);
	}
	
	public static class Builder {
		QualifiedName name;
		Domain domain;
		Statement init;
		Proposition goal;
		List<Constant> objects = Lists.newArrayList();
		
		Builder(Domain domain) {
			Objects.requireNonNull(domain);
			this.domain = domain;
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
		
		public Builder init(Statement init) {
			Objects.requireNonNull(init);
			this.init = init;
			return this;
		}

		public Builder goal(Proposition goal) {
			Objects.requireNonNull(goal);
			this.goal = goal;
			return this;
		}
		
		public Builder object(Constant object) {
			Objects.requireNonNull(object);
			objects.add(object);
			return this;
		}
		
		public Builder object(String name) {
			return object(Constant.newInstance(name));
		}

		public Builder objects(Iterable<Constant> objects) {
			Objects.requireNonNull(objects);
			Iterables.addAll( this.objects, objects );
			return this;
		}
		
		public Builder objects(Stream<Constant> objects) {
			Objects.requireNonNull(objects);
			this.objects.addAll( objects.collect( Collectors.toList() ));
			return this;
		}

		public Builder objects(Constant... objects) {
			Objects.requireNonNull(objects);
			Iterables.addAll( this.objects, Arrays.asList(objects) );
			return this;
		}

		public Problem build() {
			if( init == null ) {
//				throw new IllegalArgumentException("Must provide init"); // needed?
			}
			if( goal == null ) {
				throw new IllegalArgumentException("Must provide goal");
			}
			if( init != null && !init.isGround() ) {
				throw new IllegalArgumentException("Initial state must be ground");
			}
			if( goal != null && !goal.isGround() ) {
				throw new IllegalArgumentException("Goal state must be ground");
			}
			
			return new Problem(domain, name, init, goal, objects);
		}
	}
	
	public Set<Constant> getAllObjects() {
		return ImmutableSet.<Constant>builder().addAll(domain.getConstants()).addAll(objects).build();
	}

	public Set<Constant> getObjects() {
		return ImmutableSet.copyOf(objects);
	}
}
