package just.forward.api.statement;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import just.forward.api.expression.Variable;

public abstract class QuantifiedStatement extends AbstractCompoundStatement {

	protected ImmutableSet<Variable> variables;

	protected QuantifiedStatement(Iterable<Variable> variables, Statement statement) {
		super(ImmutableList.of(statement));
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
		return getChildren().get(0).freeVariables().filter(e -> !variables.contains(e));
	}

	public Statement getChild() {
		return getChildren().get(0);
	}
	
	@Override
	public Statement removeQuantifiers() {
		return getChild().removeQuantifiers();
	}
}
