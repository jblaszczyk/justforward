package just.forward.api.proposition;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import just.forward.api.expression.Variable;

public abstract class QuantifiedProposition extends AbstractCompoundProposition {

	protected ImmutableSet<Variable> variables;

	protected QuantifiedProposition(Iterable<Variable> variables, Proposition proposition) {
		super(ImmutableList.of(proposition));
		Objects.requireNonNull(variables);
		this.variables = ImmutableSet.copyOf(variables);
	}
	
	public ImmutableSet<Variable> getVariables() {
		return variables;
	}
	
	public Stream<Variable> variables() {
		return variables.stream();
	}
	
	@Override
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}
	@Override
	public Stream<Variable> freeVariables() {
		return getChild().freeVariables().filter(e -> !variables.contains(e));
	}

	@Override
	public boolean isPNF() {
		return getChild().isPNF();
	}
	
	@Override
	public boolean isDNF() {
		if( contains(OrProposition.class) ) {
			return false;
		}
		
		return getChild().isDNF();
	}

	public Proposition getChild() {
		return getChildren().iterator().next();
	}

	@Override
	public Proposition removeQuantifiers() {
		return getChild();
	}

}
