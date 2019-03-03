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
public final class Predicate {

	private final boolean isSynthetic;
	private final QualifiedName name;
//	private final ImmutableList<Variable> parameters;
	private final Parameters parameters;
	
	private Predicate(boolean isSynthetic, QualifiedName name, Parameters parameters) {
		Objects.requireNonNull(name);
        Objects.requireNonNull(parameters);
        this.isSynthetic = isSynthetic;
		this.name = name;
		this.parameters = parameters;
	}

	public static Predicate newInstance(QualifiedName name, Parameters parameters) {
		return new Predicate(false, name, parameters);
	}

	public static Predicate newInstance(QualifiedName name, List<? extends Variable> parameters) {
		return newInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newInstance(QualifiedName name, Stream<? extends Variable> parameters) {
		return newInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newInstance(QualifiedName name, Variable... parameters) {
		return newInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newInstance(String name, Variable... parameters) {
		return newInstance(QualifiedName.of(name), Parameters.newInstance(parameters));
	}

	public static Predicate newSyntheticInstance(QualifiedName name, Parameters parameters) {
		return new Predicate(true, name, parameters);
	}
	
	public static Predicate newSyntheticInstance(QualifiedName name, List<? extends Variable> parameters) {
		return newSyntheticInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newSyntheticInstance(QualifiedName name, Stream<? extends Variable> parameters) {
		return newSyntheticInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newSyntheticInstance(QualifiedName name, Variable... parameters) {
		return newSyntheticInstance(name, Parameters.newInstance(parameters));
	}

	public static Predicate newSyntheticInstance(String name, Variable... parameters) {
		return newSyntheticInstance(QualifiedName.of(name), Parameters.newInstance(parameters));
	}

	public boolean isSynthetic() {
		return isSynthetic;
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public List<Variable> getParameters() {
		return parameters.getVariables();
	}
	
	public Stream<Variable> parameters() {
		return parameters.variables();
	}

	public Parameters getParametersStruct() {
		return parameters;
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
		
		Predicate other = (Predicate) obj;
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
