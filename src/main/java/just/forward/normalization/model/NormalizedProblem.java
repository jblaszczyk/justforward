package just.forward.normalization.model;

import java.util.Arrays;
import java.util.Collection;
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
import just.forward.api.expression.Constant;
import just.forward.api.expression.Variable;
import just.forward.api.model.Problem;
import just.forward.api.proposition.Literal;

/*
	The goal formula is a conjunction of literals
	TODO: All axiom bodies are conjunctions of literals (except for the possible implicit existential quantification of free variables not occurring in the axiom head).
 */
@Immutable
public class NormalizedProblem {

	private final ImmutableSet<Literal> init;
	private final ImmutableSet<Literal> goal;
	private final NormalizedDomain domain;
	private final ImmutableSet<Constant> objects;
	private final Problem originalProblem;
	
	@Nullable 
	private final QualifiedName name;
	
	public NormalizedProblem(@Nullable QualifiedName name, @Nullable Problem originalProblem, NormalizedDomain domain, Iterable<Constant> objects, Iterable<Literal> init, Iterable<Literal> goal) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(domain);
		Objects.requireNonNull(init);
		Objects.requireNonNull(goal);
		this.name = name;
		this.originalProblem = originalProblem;
		this.init = ImmutableSet.copyOf(init);
		this.goal = ImmutableSet.copyOf(goal);
		this.objects = ImmutableSet.copyOf( objects );
		this.domain = domain;
	}
	
	public static Builder builder(NormalizedDomain domain) {
		return new Builder(domain);
	}
	
	public Problem getOriginal() {
		return originalProblem;
	}
	
	public Stream<Literal> init() {
		return init.stream();
	}
	
	public Stream<Literal> goal() {
		return goal.stream();
	}
	
	public NormalizedDomain getDomain() {
		return domain;
	}
	
	public QualifiedName getName() {
		return name;
	}

	public Stream<Constant> objects() {
		return objects.stream();
	}
	
	public Stream<Constant> allObjects() {
		return Stream.concat( domain.constants(), objects() );
	}

	public static class Builder {
		Problem originalProblem;
		List<Literal> init = Lists.newArrayList();
		List<Literal> goal = Lists.newArrayList();
		List<Constant> objects = Lists.newArrayList();
		NormalizedDomain domain;
		QualifiedName name;
		
		Builder(NormalizedDomain domain) {
			Objects.requireNonNull(domain);
			this.domain = domain;
		}
	
		public Builder original(Problem original) {
			Objects.requireNonNull(original);
			this.originalProblem = original;
			return this;
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
		
		public Builder object(Constant object) {
			Objects.requireNonNull(object);
			objects.add(object);
			return this;
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
		
		public Builder init(Literal init) {
			Objects.requireNonNull(init);
			if( !init.isGround() ) throw new IllegalArgumentException("Initial state atom must be ground");
			
			this.init.add(init);
			return this;
		}

		public Builder init(Iterable<Literal> init) {
			Objects.requireNonNull(init);

			List<Literal> inits = Lists.newArrayList(init);
			if( findUnground(inits) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}
			
			Iterables.addAll( this.init, inits );
			return this;
		}
		
		public Builder init(Stream<Literal> init) {
			Objects.requireNonNull(init);

			List<Literal> inits = init.collect( Collectors.toList() );
			if( findUnground(inits) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}
			
			this.init.addAll( inits );
			return this;
		}

		public Builder init(Literal... init) {
			Objects.requireNonNull(init);

			List<Literal> inits = Arrays.asList(init);
			if( findUnground(inits) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}

			Iterables.addAll( this.init, inits );
			return this;
		}
		
		public Builder goal(Literal goal) {
			Objects.requireNonNull(goal);
			if( !goal.isGround() ) throw new IllegalArgumentException("Goal state atom must be ground");

			this.goal.add(goal);
			return this;
		}
		
		public Builder goal(Iterable<Literal> goal) {
			Objects.requireNonNull(goal);
			
			List<Literal> goals = Lists.newArrayList(goal);
			if( findUnground(goals) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}
			
			this.goal.addAll(goals);
			return this;
		}
		
		public Builder goal(Stream<Literal> goal) {
			Objects.requireNonNull(goal);
			
			List<Literal> goals = goal.collect( Collectors.toList() );
			if( findUnground(goals) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}
			
			this.goal.addAll(goals);
			return this;
		}
		
		public Builder goal(Literal... goal) {
			Objects.requireNonNull(goal);

			List<Literal> goals = Arrays.asList(goal);
			if( findUnground(goals) ) {
				throw new IllegalArgumentException("Goal state atom must be ground");
			}
			
			Iterables.addAll( this.goal, goals );
			return this;
		}
		
		private boolean findUnground(Collection<Literal> literals) {
			return literals.stream().anyMatch( e -> !e.isGround() );
		}
		
		public NormalizedProblem build() {
			if( init == null ) {
//				throw new IllegalArgumentException("Must provide init"); // needed?
			}
			
			if( goal == null ) {
				throw new IllegalArgumentException("Must provide goal");
			}

			if( init != null ) {
				if( findUnground(init) ) {
					throw new IllegalArgumentException("Initial state must be ground");
				}
			}

			if( goal != null ) {
				if( findUnground(goal) ) {
					throw new IllegalArgumentException("Goal state must be ground");
				}
			}

			return new NormalizedProblem(name, originalProblem, domain, objects, init, goal);
		}
	}
	
	public Set<Literal> getInit() {
		return init;
	}
	public Set<Literal> getGoal() {
		return goal;
	}
	public Set<Constant> getObjects() {
		return objects;
	}
	public Set<Constant> getAllObjects() {
		return allObjects().collect(Collectors.toSet());
	}

	public Stream<Constant> findForVariable(Variable var) {
		return allObjects();
	}
}
