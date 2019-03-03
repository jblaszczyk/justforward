package just.forward.api.common;

import java.util.Map;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import just.forward.api.expression.Term;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.Statement;

@Immutable
public class Substitution {

	ImmutableMap<Proposition, Proposition> propositionMap;
	ImmutableMap<Statement, Statement> statementMap;
	ImmutableMap<Term, Term> termMap;
	
	protected Substitution() {}
	protected Substitution(Map<Proposition, Proposition> propositionMap, Map<Statement, Statement> statementMap, Map<Term, Term> termMap) {
		Objects.requireNonNull(propositionMap);
		Objects.requireNonNull(statementMap);
		Objects.requireNonNull(termMap);
		this.propositionMap = ImmutableMap.copyOf(propositionMap);
		this.statementMap = ImmutableMap.copyOf(statementMap);
		this.termMap = ImmutableMap.copyOf(termMap);
	}	
	
	public static Builder builder() {
		return new Builder();
	}
	
	public boolean contains(Proposition key) {
		Objects.requireNonNull(key);
		return propositionMap.containsKey(key);
	}
	
	public boolean contains(Statement key) {
		Objects.requireNonNull(key);
		return statementMap.containsKey(key);
	}

	public boolean contains(Term key) {
		Objects.requireNonNull(key);
		return termMap.containsKey(key);
	}
	
	public Proposition get(Proposition key) {
		Objects.requireNonNull(key);
		if( propositionMap.containsKey(key) ) {
			return propositionMap.get(key);
		}
		
		return key;
	}

	public Statement get(Statement key) {
		Objects.requireNonNull(key);
		if( statementMap.containsKey(key) ) {
			return statementMap.get(key);
		}
	
		return key;
	}

	public Term get(Term key) {
		Objects.requireNonNull(key);
		if( termMap.containsKey(key) ) {
			return termMap.get(key);
		}
		
		return key;
	}

	public static class Builder {
		Map<Proposition, Proposition> propositionMap = Maps.newHashMap();
		Map<Statement, Statement> statementMap = Maps.newHashMap();
		Map<Term, Term> termMap = Maps.newHashMap();

		protected Builder() {}
		
		public Builder from(Substitution other) {
			Objects.requireNonNull(other);
			propositionMap.putAll(other.propositionMap);
			statementMap.putAll(other.statementMap);
			termMap.putAll(other.termMap);
			
			return this;
		}

		public Builder map(Proposition key, Proposition value) {
			Objects.requireNonNull(key);
			Objects.requireNonNull(value);
			propositionMap.put(key, value);
			
			return this;
		}
		
		public Builder map(Statement key, Statement value) {
			Objects.requireNonNull(key);
			Objects.requireNonNull(value);
			statementMap.put(key, value);
			
			return this;
		}

		public Builder map(Term key, Term value) {
			Objects.requireNonNull(key);
			Objects.requireNonNull(value);
			termMap.put(key, value);
			
			return this;
		}

		public Substitution build() {
			return new Substitution(propositionMap, statementMap, termMap);
		}
	}
}
