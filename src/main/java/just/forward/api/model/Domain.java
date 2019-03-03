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
import just.forward.api.expression.Constant;
import just.forward.api.proposition.Rule;

@Immutable
public class Domain extends ProblemElement {

	@Nullable 
	private final QualifiedName name;
	
	private final ImmutableSet<Constant> constants;
	private final ImmutableSet<Operator> operators;
	private final ImmutableSet<Rule> rules;
	
	private Domain(@Nullable QualifiedName name, Iterable<Constant> constants, Iterable<Operator> operators, Iterable<Rule> rules) {
//		Objects.requireNonNull(name);
		Objects.requireNonNull(constants);
		Objects.requireNonNull(operators);
		Objects.requireNonNull(rules);
		this.name = name;
		this.constants = ImmutableSet.copyOf(constants);
		this.operators = ImmutableSet.copyOf(operators);
		this.rules = ImmutableSet.copyOf(rules);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public Stream<Rule> rules() {
		return rules.stream();
	}
	
	public Stream<Constant> constants() {
		return constants.stream();
	}

	public Stream<Operator> operators() {
		return operators.stream();
	}

	@Override
	public void accept(ModelVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterDomain(this) ) {
			operators.forEach(e -> e.accept(visitor) );
			visitor.leaveDomain(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				name,
				constants,
				operators,
				rules
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
		
		Domain other = (Domain) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.constants, other.constants) &&
				Objects.equals(this.operators, other.operators) &&
				Objects.equals(this.rules, other.rules);
	}
	
	public static class Builder {
		
		QualifiedName name;
		
		List<Constant> constants = Lists.newArrayList();
		List<Operator> operators = Lists.newArrayList();
		List<Rule> rules = Lists.newArrayList();

		Builder() {}

		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}
		
		public Builder constant(Constant constant) {
			Objects.requireNonNull(constant);
			constants.add(constant);
			return this;
		}

		public Builder constant(String name) {
			return constant(Constant.newInstance(name));
		}
		
		public Builder constants(Iterable<Constant> constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, constants );
			return this;
		}
		
		public Builder constants(Stream<Constant> constants) {
			Objects.requireNonNull(constants);
			this.constants.addAll( constants.collect( Collectors.toList() ));
			return this;
		}

		public Builder constants(Constant... constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, Arrays.asList(constants) );
			return this;
		}
		
		public Builder operator(Operator operator) {
			Objects.requireNonNull(operator);
			operators.add(operator);
			return this;
		}
		
		public Builder operators(Iterable<Operator> operators) {
			Objects.requireNonNull(operators);
			Iterables.addAll( this.operators, operators );
			return this;
		}
		
		public Builder operators(Stream<Operator> operators) {
			Objects.requireNonNull(operators);
			this.operators.addAll( operators.collect( Collectors.toList() ));
			return this;
		}
		
		public Builder operators(Operator... operators) {
			Objects.requireNonNull(operators);
			Iterables.addAll( this.operators, Arrays.asList(operators) );
			return this;
		}
		
		public Builder rule(Rule rule) {
			Objects.requireNonNull(rule);
			rules.add(rule);
			return this;
		}
		
		public Builder rules(Iterable<Rule> rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, rules );
			return this;
		}
		
		public Builder rules(Stream<Rule> rules) {
			Objects.requireNonNull(rules);
			this.rules.addAll( rules.collect( Collectors.toList() ));
			return this;
		}
		
		public Builder rules(Rule... rules) {
			Objects.requireNonNull(rules);
			Iterables.addAll( this.rules, Arrays.asList(rules) );
			return this;
		}
		
		public Domain build() {
			if( operators.isEmpty() ) {
				throw new IllegalArgumentException("Domain should have at least one operator");
			}

			return new Domain(name, constants, operators, rules);
		}
	}
	
	public Set<Operator> getOperators() {
		return operators;
	}
	
	public Set<Constant> getConstants() {
		return constants;
	}
	
	public ImmutableSet<Rule> getRules() {
		return rules;
	}
}
