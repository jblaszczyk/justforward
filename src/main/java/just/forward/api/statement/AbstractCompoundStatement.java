package just.forward.api.statement;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import just.forward.api.common.Helper;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;

public abstract class AbstractCompoundStatement extends Statement {

	protected ImmutableList<Statement> children;


	protected AbstractCompoundStatement(Iterable<? extends Statement> children) {
		Objects.requireNonNull(children);
		this.children = ImmutableList.copyOf( Helper.cleanStatements(children) );
	}
	
	public List<Statement> getChildren() {
		return children;
	}
	public Stream<Statement> children() {
		return getChildren().stream(); 
	}	
	
	protected List<Statement> groundChildren(VariableSubstitution subsitution) {
		return children().map(e -> e.ground(subsitution)).collect(Collectors.toList());
	}	
	
	@Override
	public boolean isNNF() {
		return children().allMatch(Statement::isNNF);
	}

	@Override
	public boolean isWhenNF() {
		return children().allMatch(Statement::isWhenNF);
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}

	@Override
	public boolean contains(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return children().anyMatch( e -> e.uses(clazz));
	}
	
	@Override
	public boolean uses(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz.isInstance(this) || contains(clazz);
	}
	
	
}
