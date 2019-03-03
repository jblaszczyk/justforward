package just.forward.api.statement;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import just.forward.api.common.Helper;
import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;

public abstract class Statement {

	@Deprecated
	public abstract boolean isNNF();

	public abstract boolean isEmpty();
	public abstract boolean isPNF();
//	public abstract boolean isCNF(); TODO
	public abstract boolean isWhenNF();
//	public abstract boolean isSimplified(); TODO
 	
	
	public abstract Statement flatten(); // TODO
	public abstract Statement reduce(); // TODO
	public abstract Statement emptify(); // TODO
	public abstract Statement toPNF(); // not needed?

	public Statement toWhenNF() {
		return Helper.repeatUntilStable(this, 
			e -> e.pullAndOverForAll().simplify()  
			.pushWhenOverForall().simplify()
			.pushWhenOverOr().simplify()
		);
	}
	
	public Statement toCNF() {
		return Helper.repeatUntilStable(this, 
			e -> e.pullAndOverForAll().simplify()
			.pullAndOverWhen().simplify()
		);
	}


	protected abstract Statement pullAndOverForAll();
	protected abstract Statement pullAndOverWhen();
	protected abstract Statement pushWhenOverForall();
	protected abstract Statement pushWhenOverOr();
	
	
	// all variables = free vars + forall vars + exists vars in WhenStatement
	public Set<Variable> getAllVariables() {
		return allVariables().collect(Collectors.toSet());
	}
	public Stream<Variable> allVariables() {
		return Stream.concat(freeVariables(), quantifiedVariables());
	}
	public Set<Variable> getQuantifiedVariables() {
		return quantifiedVariables().collect(Collectors.toSet());
	}	
	public Stream<Variable> quantifiedVariables() {
		return Stream.concat(universallyQuantifiedVariables(), existentiallyQuantifiedVariables());
	}

	public abstract Set<Variable> getFreeVariables();
	public abstract Stream<Variable> freeVariables();
	
	public abstract Set<Variable> getUniversallyQuantifiedVariables();
	public abstract Stream<Variable> universallyQuantifiedVariables();

	public abstract Set<Variable> getExistentiallyQuantifiedVariables();
	public abstract Stream<Variable> existentiallyQuantifiedVariables();

	public abstract Statement removeQuantifiers();
	
	public abstract boolean isGround();
	public abstract Statement ground(VariableSubstitution substitution);
	public abstract Statement substitute(Substitution substitution);
	
	public abstract boolean contains(Class<?> clazz);
	public abstract boolean uses(Class<?> clazz);

	public Statement simplify() {
		return flatten().reduce().emptify(); 
	}

	public abstract void accept(StatementVisitor visitor);

}
