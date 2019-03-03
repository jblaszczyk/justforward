package just.forward.api.statement;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

import just.forward.api.expression.Variable;
import just.forward.api.proposition.EmptyProposition;
import just.forward.api.proposition.Proposition;

public abstract class Assignment extends Statement {

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean isNNF() {
		return true;
	}

	@Override
	public boolean isPNF() {
		return true;
	}

	@Override
	public boolean isWhenNF() {
		return true;
	}

	@Override
	public Assignment toWhenNF() {
		return this;
	}

	@Override
	public Assignment toPNF() {
		return this;
	}
	
	@Override
	public Statement toCNF() {
		return this;
	}
/*	
	@Override
	public Statement simplify() {
		if( isEmpty() ) {
			return EmptyStatement.getInstance();
		}
		
		return this;
	}
*/
	@Override
	public Assignment flatten() {
		return this;
	}

	@Override
	public Assignment reduce() {
		return this;
	}

	@Override
	public Assignment emptify() {
		return this;
	}

	@Override
	public Assignment removeQuantifiers() {
		return this;
	}

	@Override
	public Set<Variable> getQuantifiedVariables() {
		return ImmutableSet.of();
	}	
	@Override
	public Stream<Variable> quantifiedVariables() {
		return Stream.of();
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return ImmutableSet.of();
	}	
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return Stream.of();
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return ImmutableSet.of();
	}	
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return Stream.of();
	}

	@Override
	protected Assignment pullAndOverForAll() {
		return this;
	}
	
	@Override
	protected Assignment pullAndOverWhen() {
		return this;
	}
	
	@Override
	protected Assignment pushWhenOverForall() {
		return this;
	}
	
	@Override
	protected Assignment pushWhenOverOr() {
		return this;
	}
}
