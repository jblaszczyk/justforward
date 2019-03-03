package just.forward.sas.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Constant;

@Deprecated
public class SASProblem {

//	Problem originalProblem;
	SASPartialAssignmentImmutable init;
	SASPartialAssignmentImmutable goal;
	SASDomain domain;
	ImmutableSet<Constant> problemObjects;
	QualifiedName name;
	
	SASProblem(SASDomain domain, QualifiedName name, Iterable<Constant> problemObjects, AbstractSASPartialAssignment init, AbstractSASPartialAssignment goal) {
		Objects.requireNonNull(domain);
		Objects.requireNonNull(init);
		Objects.requireNonNull(goal);
		Objects.requireNonNull(name);
		this.name = name;
		this.init = SASPartialAssignmentImmutable.copyOf(init);
		this.goal = SASPartialAssignmentImmutable.copyOf(goal);
		this.domain = domain;
		this.problemObjects = ImmutableSet.copyOf(problemObjects);
	}

	public static Builder builder(SASDomain domain) {
		return new Builder(domain);
	}
	
	public AbstractSASPartialAssignment getInit() {
		return init;
	}
	
	public AbstractSASPartialAssignment getGoal() {
		return goal;
	}
	
	public SASDomain getDomain() {
		return domain;
	}
	
	public QualifiedName getName() {
		return name;
	}

	public Set<Constant> getObjects() {
		return problemObjects;
	}
	
	public Set<Constant> getAllObjects() {
		return allObjects().collect(Collectors.toSet());
	}
	
	public Stream<Constant> objects() {
		return problemObjects.stream();
	}
	
	public Stream<Constant> allObjects() {
		return Stream.concat( domain.constants(), objects());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				problemObjects,
				init,
				goal,
				domain
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
		
		SASProblem other = (SASProblem) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.problemObjects, other.problemObjects) &&
				Objects.equals(this.init, other.init) &&
				Objects.equals(this.goal, other.goal) &&
				Objects.equals(this.domain, other.domain);
	}
	
	public static class Builder {
		
		AbstractSASPartialAssignment init;
		AbstractSASPartialAssignment goal;
		SASDomain domain;
		LinkedList<Constant> objects = Lists.newLinkedList();
		QualifiedName name;

		Builder(SASDomain domain) {
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
		
		public Builder init(AbstractSASPartialAssignment init) {
			Objects.requireNonNull(init);
			this.init = init;
			return this;
		}
		
		public Builder goal(AbstractSASPartialAssignment goal) {
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

		public SASProblem build() {
			
			if( init == null ) {
//				throw new IllegalArgumentException("Must provide init"); // needed?
			}
			
			if( goal == null || goal.isEmpty() ) {
				throw new IllegalArgumentException("Must provide goal");
			}
			
			return new SASProblem(domain, name, objects, init, goal);
		}
	}
}
