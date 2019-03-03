package just.forward.api.expression;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

@Immutable
public class VariableSubstitution {

	private final ImmutableMap<Variable, Term> variable2term;
	
	protected VariableSubstitution(Map<Variable, Term> map) {
		Objects.requireNonNull(map);
		this.variable2term = ImmutableMap.copyOf(map);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public Term findValue(Variable var) {
		Objects.requireNonNull(var);
		return variable2term.get(var);
	}
	
	public boolean contains(Variable var) {
		Objects.requireNonNull(var);
		return variable2term.containsKey(var);
	}

	public boolean containsAll(Collection<Variable> vars) {
		Objects.requireNonNull(vars);
		return variable2term.keySet().containsAll(vars);
	}
	
	public Set<Variable> keySet() {
		return ImmutableSet.copyOf(variable2term.keySet());
	}

	public static class Builder {
		Map<Variable, Term> variable2term = Maps.newHashMap();

		Builder() {}

		public Builder from(Map<Variable, Term> other) {
			Objects.requireNonNull(other);
			variable2term.putAll(other);
			return this;
		}
		
		public Builder from(VariableSubstitution other) {
			Objects.requireNonNull(other);
			variable2term.putAll(other.variable2term);
			return this;
		}

		public Builder map(Variable key, Term value) {
			Objects.requireNonNull(key);
			Objects.requireNonNull(value);
			variable2term.put(key, value);
			
			return this;
		}
		
		public Builder remove(Variable key) {
			Objects.requireNonNull(key);
			variable2term.remove(key);
			return this;
		}
		
		public VariableSubstitution build() {
			return new VariableSubstitution(variable2term);
		}
	}
}
