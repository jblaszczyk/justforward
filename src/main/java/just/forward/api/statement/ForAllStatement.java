package just.forward.api.statement;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import just.forward.api.common.Helper;
import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.EmptyProposition;
import just.forward.api.proposition.ForAllProposition;
import just.forward.api.proposition.Proposition;

@Immutable
public final class ForAllStatement extends QuantifiedStatement {
	
	private ForAllStatement(Iterable<Variable> variables, Statement statement) {
		super(variables, statement);
	}
	
	public static ForAllStatement newInstance(Iterable<Variable> variables, Statement statement) {
		return new ForAllStatement(variables, statement);
	}

	public static ForAllStatement newInstance(Stream<Variable> variables, Statement statement) {
		return newInstance(variables.collect(Collectors.toSet()), statement);
	}

	@Override
	public Statement substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return ForAllStatement.newInstance(
			variables()
				.map(e -> e.substitute(substitution))
				.filter(e -> e instanceof Variable)
				.map(e -> (Variable) e), 
			getChild().substitute(substitution)
		);
	}

	@Override
	public boolean isGround() {
		return getChild().isGround();
	}

	@Override
	public Statement ground(VariableSubstitution substitution) {
		Statement groundChild = getChild().ground(substitution);
		
		if( substitution.containsAll(variables) ) {
			return groundChild;
		}
		
		Set<Variable> ungroundVariables = Sets.newHashSet(variables);
		ungroundVariables.removeAll(substitution.keySet());
		
		return new ForAllStatement(ungroundVariables, groundChild);
	}

	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return variables;
	}
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return variables();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return getChild().getExistentiallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return getChild().existentiallyQuantifiedVariables();
	}

	@Override
	public boolean isPNF() {
		return getChild().isPNF();
	}

/*	@Override
	public ForAllStatement toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return ForAllStatement.newInstance(variables, getChild().toNNF());
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
	
/*	@Override
	public Statement toWhenNF() {
		if( isWhenNF() ) {
			return this;
		}
		
		return ForAllStatement.newInstance(variables, getChild().toWhenNF());
	}
*/	
	@Override
	protected Statement pullAndOverForAll() {
		// forall(x, and(p,q)) ==> and(forall(x,p),  forall(x,q))
//		Statement child = getChild().pullAndOverForAll();
		Statement child = Helper.repeatUntilStable(getChild(), Statement::pullAndOverForAll).simplify();

		if( child instanceof AndStatement ) {
			AndStatement and = (AndStatement) child;
			
			Statement result = AndStatement.newInstance(and.children()
				.map(e -> ForAllStatement.newInstance(variables, e))
			);
			
			return Helper.repeatUntilStable(result, Statement::pullAndOverForAll).simplify();
		}
		
		return ForAllStatement.newInstance(variables, child).simplify();
	}
	
	@Override
	protected Statement pullAndOverWhen() {
		// forall(x, when(phi, and(p,q)) ==> forall(x, and(when(phi,p), when(phi,q))
		return ForAllStatement.newInstance(
			variables,
//			getChild().pullAndOverWhen()
			Helper.repeatUntilStable(getChild(), Statement::pullAndOverWhen)
		).simplify();
	}
	
	@Override
	protected Statement pushWhenOverForall() {
		// when(phi, forall(x,p)) ==> forall(x,when(phi,p))
		
		return ForAllStatement.newInstance(
			variables,
//			getChild().pushWhenOverForall()
			Helper.repeatUntilStable(getChild(), Statement::pushWhenOverForall)
		).simplify();
	}

	@Override
	protected Statement pushWhenOverOr() {
		return ForAllStatement.newInstance(
			variables,
//			getChild().pushWhenOverOr()
			Helper.repeatUntilStable(getChild(), Statement::pushWhenOverOr)

		).simplify();
	}
	
/*	@Override
	public ForAllStatement toNegated() {
		return ForAllStatement.newInstance(variables, getChild().toNegated());
	}
*/	
/*
	@Override
	public Statement simplify() {
		Statement newChild = getChild().simplify();
		if( newChild.isEmpty() ) {
			return EmptyStatement.getInstance();
		}
		
		if( variables.isEmpty() ) {
			return newChild;
		}
		
		return ForAllStatement.newInstance(variables, newChild);
	}
*/	
	@Override
	public ForAllStatement flatten() {
		Statement child = getChild().flatten();
		
		if( child instanceof ForAllStatement ) {
			ForAllStatement inner = (ForAllStatement) child;
			
			ForAllStatement result = ForAllStatement.newInstance(
				Stream.concat(variables(), inner.variables()), 
				inner.getChild()
			);
			
			return Helper.repeatUntilStable(result, ForAllStatement::flatten);
		}

		return ForAllStatement.newInstance(
			variables,
			child
		);
	}

	@Override
	public Statement reduce() {
		Statement child = getChild().reduce();
		
		if( variables.isEmpty() ) {
			return child;
		}
		
		return ForAllStatement.newInstance(variables, child);
	}

	@Override
	public Statement emptify() {
		Statement child = getChild().emptify();
		
		if( child.isEmpty() ) {
			return EmptyStatement.getInstance();
		}
		
		return ForAllStatement.newInstance(variables, child);
	}

/*	
	@Override
	public Statement toCNF() {
		// TODO Auto-generated method stub
		throw new AssertionError("Not Implemented.");
	}
*/
	@Override
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterForAllStatement(this) ) {
			getChild().accept(visitor);
			visitor.leaveForAllStatement(this);
		}
	}
	
	@Override
	public String toString() {
		return "forall[" + Joiner.on(',').join(variables) + "](" + getChild().toString() +")";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				getChildren()
		);
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		// TODO ForAllProposition in StructuralComparator
		ForAllStatement other = (ForAllStatement) obj;
		return Objects.equals(this.getChildren(), other.getChildren());
	}

}
