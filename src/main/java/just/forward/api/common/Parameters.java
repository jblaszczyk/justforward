package just.forward.api.common;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import just.forward.api.expression.Tuple;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;

@Immutable
public class Parameters {

	private final ImmutableList<Variable> parameters;
	
	private Parameters(List<? extends Variable> parameters) {
		Objects.requireNonNull(parameters);
		this.parameters = ImmutableList.copyOf(parameters);
	}
	
	public static Parameters newInstance(List<? extends Variable> parameters) {
		return new Parameters(parameters);
	}
	
	public static Parameters newInstance(Stream<? extends Variable> parameters) {
		return newInstance(parameters.collect(Collectors.toList()));
	}
	
	public static Parameters newInstance(Variable... parameters) {
		return newInstance(Arrays.asList(parameters));
	}
	
	public List<Variable> getVariables() {
		return parameters;
	}
	
	public Stream<Variable> variables() {
		return parameters.stream();
	}
	
	public Variable getVariable(int i) {
		return parameters.get(i);
	}
	
	public Set<Variable> getFreeVariables() {
		return Sets.newHashSet(parameters);
	}
	
	public Stream<Variable> freeVariables() {
		return parameters.stream();
	}
	
	public boolean isGround() {
		return false;
	}
	
	public Tuple ground(VariableSubstitution substitution) {
		return Tuple.newInstance(
			variables()
				.map(e -> e.ground(substitution))
				.collect(Collectors.toList())
		);
	}
	
	public Tuple substitute(Substitution substitution) {
		return Tuple.newInstance(
			variables()
				.map(e -> e.substitute(substitution))
				.collect(Collectors.toList())
		);
	}
	
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return variables()
			.anyMatch(e -> e.uses(clazz));
	}
	
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}

	@Override
	public String toString() {
		return '(' +  Joiner.on(',').join(parameters) + ')';
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Parameters other = (Parameters) obj;
		
		return Objects.equals(this.parameters, other.parameters);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				parameters
		);
	}
}
