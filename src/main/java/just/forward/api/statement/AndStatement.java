package just.forward.api.statement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import just.forward.api.common.Helper;
import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.AndProposition;
import just.forward.api.proposition.BooleanLiteral;
import just.forward.api.proposition.EmptyProposition;
import just.forward.api.proposition.Proposition;
import just.forward.api.proposition.PropositionVisitor;
import just.forward.api.proposition.QuantifiedProposition;

@Immutable
public final class AndStatement extends AbstractCompoundStatement {
	
	private AndStatement(Iterable<? extends Statement> children) {
		super(children);
	}
	
	public static AndStatement newInstance(Iterable<? extends Statement> children) {
		return newInstance(ImmutableList.copyOf(children).stream());
	}
	
	public static AndStatement newInstance(Statement... children) {
		return newInstance(Stream.of(children));
	}

	public static AndStatement newInstance(Stream<? extends Statement> children) {
		List<? extends Statement> op = children.collect(Collectors.toList());
		if( op.isEmpty() ) {
			throw new IllegalArgumentException("AndStatement must have at least one operand");
		}
		
		return new AndStatement(op);
	}
	
	@Override
	public Statement substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return AndStatement.newInstance(
			children().map(e -> e.substitute(substitution))
		);
	}
	
	public AndStatement merge(AndStatement other) {
		if( other.isEmpty() ) {
			return this;
		}
		
		List<Statement> args = Lists.newArrayList(getChildren());
		args.addAll(other.getChildren());
		
		return AndStatement.newInstance(args);
	}

	@Override
	public boolean isGround() {
		return children().allMatch(Statement::isGround);
	}

	@Override
	public AndStatement ground(VariableSubstitution subsitution) {
		return AndStatement.newInstance(groundChildren(subsitution));
	}
	
	@Override
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}	
	@Override
	public Stream<Variable> freeVariables() {
		return children().flatMap(Statement::freeVariables);
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return children().flatMap(Statement::universallyQuantifiedVariables);
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return existentiallyQuantifiedVariables().collect(Collectors.toSet());
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return children().flatMap(Statement::existentiallyQuantifiedVariables);
	}

	@Override
	public boolean isPNF() {
		return !contains(QuantifiedStatement.class);
	}

/*	@Override
	public AndStatement toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return AndStatement.newInstance(children().map(Statement::toNNF));
	}
	@Override
	public Statement toCNF() {
//		if( isCNF() ) { TODO
//			return this;
//		}
		
		return AndStatement.newInstance(children().map(Statement::toCNF));
	}
 */	
	
/*	@Override
	public Statement toWhenNF() {
		if( isWhenNF() ) {
			return this;
		}
		
		return AndStatement.newInstance(
				children().map(Statement::toWhenNF)
		);
	}
*/
	@Override
	public Statement toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		return ForAllStatement.newInstance(quantifiedVariables(), 
			this.removeQuantifiers()
		).simplify();
	}

	
	@Override
	public Statement removeQuantifiers() {
		if( !contains(QuantifiedStatement.class) ) {
			return this;
		}
		
		return AndStatement.newInstance(
			children()
				.map(Statement::removeQuantifiers)
		).simplify();
	}
	
	@Override
	protected Statement pullAndOverForAll() {
		return AndStatement.newInstance(
			children()
//				.map(Statement::pullAndOverForAll)
				.map(e -> Helper.repeatUntilStable(e, Statement::pullAndOverForAll))
		).simplify();
	}
	
	@Override
	protected Statement pullAndOverWhen() {
		return AndStatement.newInstance(
			children()
//				.map(Statement::pullAndOverWhen)
				.map(e -> Helper.repeatUntilStable(e, Statement::pullAndOverWhen))
		).simplify();
	}
	
	@Override
	protected Statement pushWhenOverForall() {
		return AndStatement.newInstance(
			children()
//				.map(Statement::pushWhenOverForall)
				.map(e -> Helper.repeatUntilStable(e, Statement::pushWhenOverForall))
		).simplify();
	}

	@Override
	protected Statement pushWhenOverOr() {
		return AndStatement.newInstance(
			children()
//				.map(Statement::pushWhenOverOr)
				.map(e -> Helper.repeatUntilStable(e, Statement::pushWhenOverOr))
		).simplify();
	}
/*	@Override
	public AndStatement toNegated() {
		return AndStatement.newInstance(
			children().map(Statement::toNegated)
		);
	}
*/
	
	@Override
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterAndStatement(this) ) {
			children.forEach(e -> e.accept(visitor));
			visitor.leaveAndStatement(this);
		}
	}
	
	@Override
	public String toString() {
		return "and(" + Joiner.on(',').join(children) + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO AndProposition in StructuralComparator
		AndStatement other = (AndStatement) obj;
		return Objects.equals(this.getChildren(), other.getChildren());
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				getChildren()
		);
	}
	
	
	
	
	@Override
	public AndStatement flatten() {
		List<Statement> newChildren = children()
//			.map(Statement::flatten)
			.map(e -> Helper.repeatUntilStable(e, Statement::flatten))
			.filter(EmptyStatement.filterOut())
			.collect(Collectors.toList());

		List<Statement> newChildrenAnd = newChildren.stream()
			.filter(e -> e instanceof AndStatement)
			.map(e -> ((AndStatement) e))
			.flatMap( e -> e.children())
			.collect(Collectors.toList());

		List<Statement> newChildrenRest = newChildren.stream()
			.filter(e -> !(e instanceof AndStatement))
			.collect(Collectors.toList());
			
		newChildren = Stream.concat(newChildrenRest.stream(), newChildrenAnd.stream())
			.filter(EmptyStatement.filterOut())
//			.map(Statement::flatten)
			.collect(Collectors.toList());
		
		return AndStatement.newInstance(newChildren);
	}

	@Override
	public Statement reduce() {
		return AndStatement.newInstance(
			children()
				.map(Statement::reduce)
		);
	}

	@Override
	public Statement emptify() {
		List<Statement> newChildren = children()
			.map(Statement::emptify)
			.filter(EmptyStatement.filterOut())
			.collect(Collectors.toList());
		
		if( newChildren.isEmpty() ) {
			return EmptyStatement.getInstance();
		}
		
		return AndStatement.newInstance(newChildren);
	}

	@Deprecated
	public Statement oldSimplify() {
		List<Statement> newChildren = children()
			.map(Statement::simplify)
			.filter(EmptyStatement.filterOut())
			.collect(Collectors.toList());

		List<Statement> newChildrenAnd = newChildren.stream()
			.filter(e -> e instanceof AndStatement)
			.map(e -> ((AndStatement) e))
			.flatMap( e -> e.children())
			.collect(Collectors.toList());
			
		List<Statement> newChildrenRest = newChildren.stream()
			.filter(e -> !(e instanceof AndStatement))
			.collect(Collectors.toList());
		
		newChildren = Stream.concat(newChildrenRest.stream(), newChildrenAnd.stream())
			.filter(EmptyStatement.filterOut())
			.collect(Collectors.toList());
			
		if( newChildren.isEmpty() ) {
			return EmptyStatement.getInstance();
		}

/*		if( newChildren.contains(BooleanLiteral.getFalse()) ) { TODO remove?
			return BooleanLiteral.getFalse();
		}
		
		if( newChildren.stream().allMatch(BooleanLiteral.filterTrue()) ) {
			return BooleanLiteral.getTrue();
		}
*/
		if( newChildren.size() == 1 ) {
			return newChildren.get(0);
		}
		
/*		if( isDirectClashWith(newChildren) ) { TODO
			return ???
		}
*/		
		return AndStatement.newInstance(newChildren);
	}	
}
