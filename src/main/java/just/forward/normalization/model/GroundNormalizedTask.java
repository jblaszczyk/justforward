package just.forward.normalization.model;

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
import just.forward.api.expression.Constant;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.Literal;

@Immutable
public class GroundNormalizedTask {

	private final NormalizedProblem problem;
	private final ImmutableSet<GroundNormalizedAction> actions;
	
	private GroundNormalizedTask(NormalizedProblem problem, Iterable<GroundNormalizedAction> actions) {
		Objects.requireNonNull(problem);
		Objects.requireNonNull(actions);
		this.problem = problem;
		this.actions = ImmutableSet.copyOf(actions);
	}
	
	public static Builder builder(NormalizedProblem problem) {
		return new Builder(problem);
	}
	
	public QualifiedName getName() {
		return getProblem().getName();
	}
	
	public NormalizedProblem getProblem() {
		return problem;
	}
	
	public ImmutableSet<GroundNormalizedAction> getActions() {
		return actions;
	}
	
	public Stream<GroundNormalizedAction> actions() {
		return actions.stream();
	}

	public Set<Constant> getObjects() {
		return getProblem().getAllObjects();
	}
	
	public Stream<Constant> objects() {
		return getProblem().allObjects();
	}

	public Stream<Literal> init() {
		return getProblem().init();
	}
	
	public Stream<Literal> goal() {
		return getProblem().goal();
	}
	
	public Set<Literal> getInit() {
		return getProblem().getInit();
	}
	public Set<Literal> getGoal() {
		return getProblem().getGoal();
	}

	public Stream<Constant> findForVariable(Variable var) {
		return problem.findForVariable(var);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		GroundNormalizedTask other = (GroundNormalizedTask) obj;
		return Objects.equals(this.problem, other.problem) &&
				Objects.equals(this.actions, other.actions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass(),
				problem,
				actions
		);
	}
	public static class Builder {

		NormalizedProblem problem;
		List<GroundNormalizedAction> actions = Lists.newArrayList();
		List<GroundNormalizedRule> rules = Lists.newArrayList();
		
		Builder(NormalizedProblem problem) {
			Objects.requireNonNull(problem);
			this.problem = problem;
		}
		
		public Builder action(GroundNormalizedAction action) {
			Objects.requireNonNull(action);
			actions.add(action);
			return this;
		}
		
		public Builder actions(Iterable<GroundNormalizedAction> actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, actions );
			return this;
		}
		
		public Builder actions(Stream<GroundNormalizedAction> actions) {
			Objects.requireNonNull(actions);
			this.actions.addAll( actions.collect( Collectors.toList() ));
			return this;
		}

		public Builder actions(GroundNormalizedAction... actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, Arrays.asList(actions) );
			return this;
		}
		
		public Builder rule(GroundNormalizedRule rule) {
			Objects.requireNonNull(rule);
			this.rules.add(rule);
			return this;
		}
		
		public Builder rules(Iterable<GroundNormalizedRule> rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, rules );
			return this;
		}
		
		public Builder rules(Stream<GroundNormalizedRule> rules) {
			Objects.requireNonNull(rules);
			this.rules.addAll( rules.collect( Collectors.toList() ));
			return this;
		}

		public Builder rules(GroundNormalizedRule... rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, Arrays.asList(rules) );
			return this;
		}
		
		public GroundNormalizedTask build() {
			return new GroundNormalizedTask(problem, actions);
		}
		
	}
}
