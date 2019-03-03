package just.forward.api.common;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import just.forward.api.expression.Variable;

@Immutable
public final class Fluent {

	private final boolean isSynthetic;
	private final QualifiedName name;
	private final ImmutableList<Variable> parameters;
//	private final Type returnType; TODO 
	
	private Fluent(boolean isSynthetic, QualifiedName name, Iterable<Variable> parameters) {
		Objects.requireNonNull(name);
        Objects.requireNonNull(parameters);
        this.isSynthetic = isSynthetic;
		this.name = name;
		this.parameters = ImmutableList.copyOf(parameters);
	}

	public static Fluent newInstance(QualifiedName name, Iterable<Variable> parameters) {
		return new Fluent(false, name, parameters);
	}

	public static Fluent newInstance(QualifiedName name, Stream<Variable> parameters) {
		List<Variable> op = parameters.collect(Collectors.toList());
		return new Fluent(false, name, op);
	}

	public static Fluent newInstance(QualifiedName name, Variable... parameters) {
		return new Fluent(false, name, Arrays.asList(parameters));
	}

	public static Fluent newInstance(String name, Variable... parameters) {
		return new Fluent(false, QualifiedName.of(name), Arrays.asList(parameters));
	}

	public static Fluent newSyntheticInstance(QualifiedName name, Iterable<Variable> parameters) {
		return new Fluent(true, name, parameters);
	}

	public static Fluent newSyntheticInstance(QualifiedName name, Stream<Variable> parameters) {
		List<Variable> op = parameters.collect(Collectors.toList());
		return new Fluent(true, name, op);
	}

	public static Fluent newSyntheticInstance(QualifiedName name, Variable... parameters) {
		return new Fluent(true, name, Arrays.asList(parameters));
	}

	public static Fluent newSyntheticInstance(String name, Variable... parameters) {
		return new Fluent(true, QualifiedName.of(name), Arrays.asList(parameters));
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public ImmutableList<Variable> getParameters() {
		return parameters;
	}
	public Stream<Variable> parameters() {
		return parameters.stream();
	}

	@Override
	public String toString() {
		return name.toString() + '(' +  Joiner.on(',').join(parameters) + ')';
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Fluent other = (Fluent) obj;
		return Objects.equals(this.name, other.name) &&
				Objects.equals(this.parameters, other.parameters);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				name,
				parameters
		);
	}
}
