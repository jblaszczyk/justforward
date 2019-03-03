package just.forward.sas.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import just.forward.api.proposition.Literal;

public abstract class AbstractSASPartialAssignment {

	protected Map<SASVariable, Literal> map = Maps.newHashMap();
	
	protected AbstractSASPartialAssignment() {
	}

	protected AbstractSASPartialAssignment(AbstractSASPartialAssignment other) {
		this.map = Maps.newHashMap(other.map);
	}

	public Literal findValue(SASVariable var) {
		Objects.requireNonNull(var);
		if( map.containsKey(var) ) {
			return map.get(var);
		}
		
		return null;
	}

	public boolean contains(SASVariable var) {
		Objects.requireNonNull(var);
		return map.containsKey(var);
	}

	public boolean entails(Map.Entry<SASVariable, Literal> assignment) {
		return entails(assignment.getKey(), assignment.getValue());
	}
	
	public boolean entails(SASVariable var, Literal value) {
		Objects.requireNonNull(var);
		Objects.requireNonNull(value);
		if( map.containsKey(var) ) {
			Literal sasvalue = map.get(var);
			return value.equals(sasvalue);
		}
		
		return false;
	}

	public boolean entailsAll(AbstractSASPartialAssignment other) {
		for(Entry<SASVariable, Literal> kv : other.map.entrySet()) {
			SASVariable var = kv.getKey();
			if( !map.containsKey(var) ) {
				return false;
			}
			
			Literal otherValue = kv.getValue();
			if( !otherValue.equals( map.get(var) )) {
				return false;
			}
		}
		
		return true;
	}
	
	public Set<SASVariable> getVariables() {
		return map.keySet();
	}
	
	public Stream<SASVariable> variables() {
		return getVariables().stream();
	}
	
	public Set<Map.Entry<SASVariable, Literal>> getEntries() {
		return map.entrySet();
	}
	
	public Stream<Map.Entry<SASVariable, Literal>> entries() {
		return map.entrySet().stream();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(map);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		AbstractSASPartialAssignment other = (AbstractSASPartialAssignment) obj;
		return Objects.equals(this.map, other.map);
	}
	
	@Override
	public String toString() {
		List<String> args = map.entrySet().stream().map(e -> "[" + e.getKey() + "] = [" + e.getValue() + "]").collect(Collectors.toList());
		return Joiner.on('\n').join(args);
	}
}