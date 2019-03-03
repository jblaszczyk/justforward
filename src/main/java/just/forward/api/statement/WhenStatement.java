package just.forward.api.statement;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

import just.forward.api.common.Helper;
import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.AndProposition;
import just.forward.api.proposition.BooleanLiteral;
import just.forward.api.proposition.OrProposition;
import just.forward.api.proposition.Proposition;
import just.forward.api.proposition.QuantifiedProposition;

@Immutable
public final class WhenStatement extends AbstractCompoundStatement {

	private final Proposition when;
	
	private WhenStatement(Proposition when, Statement then) {
		super( ImmutableList.of(then) );
		Objects.requireNonNull(when);
		Objects.requireNonNull(then);
		this.when = when;
	}
	
	public static WhenStatement newInstance(Proposition when, Statement then) {
		return new WhenStatement(when, then);
	}
	
	@Override
	public Statement substitute(Substitution substitution) {
		if( substitution.contains(this) ) {
			return substitution.get(this);
		}
		
		return WhenStatement.newInstance(getWhen().substitute(substitution), getThen().substitute(substitution));
	}
	
	public Proposition getWhen() {
		return when;
	}
	
	public Statement getThen() {
		return getChildren().get(0);
	}
	
	@Override
	public boolean isNNF() {
		return getWhen().isNNF() && getThen().isNNF();
	}

	@Override
	public boolean isPNF() {
		return !getWhen().uses(QuantifiedProposition.class) && !getThen().uses(QuantifiedStatement.class);
	}

	@Override
	public boolean isWhenNF() {
		return getThen().isWhenNF();
	}

	@Override
	public boolean isEmpty() {
		return getWhen().isEmpty() || getThen().isEmpty();
	}

/*	@Override
	public WhenStatement toNNF() {
		if( isNNF() ) {
			return this;
		}
		
		return new WhenStatement(getWhen().toNNF(), getThen().toNNF());
	}
*/
	@Override
	public Statement toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		return ForAllStatement.newInstance(quantifiedVariables(), 
			this.removeQuantifiers()
		);
	}
	
	@Deprecated
	private Statement oldToWhenNF() {
//		if( isWhenNF() ) { TODO
//			return this;
//		}
		
		if( getWhen().uses(OrProposition.class) ) {
			OrProposition inner = (OrProposition) getWhen().toDNF();
			
			return AndStatement.newInstance(
					inner.children()
						.map(e -> WhenStatement.newInstance(e.toPNF(), getThen().toWhenNF()).toWhenNF())
				);//.toWhenNF()
		}
		
		if( getThen() instanceof AndStatement ) { // when( phi, and(p,q) ) ==> and( when(phi,p), when(phi,q) )
			AndStatement inner = (AndStatement) getThen();
			
			return AndStatement.newInstance(
				inner.children()
					.map(e -> WhenStatement.newInstance(getWhen().toPNF(), e).toWhenNF())
			);//.toWhenNF()
			
		} else if( getThen() instanceof ForAllStatement ) { // when( phi, forall(x,p) ) ==> forall( x, when(phi,p) )
			ForAllStatement inner = (ForAllStatement) getThen();
		
			return ForAllStatement.newInstance(
				inner.variables(), 
				WhenStatement.newInstance(getWhen().toPNF(), inner.getChild()).toWhenNF()
			);//.toWhenNF()
			
		} else if( getThen() instanceof WhenStatement ) { // // when( phi, when(ksi,p) ) ==> when( and(phi,ksi), p )
			WhenStatement inner = (WhenStatement) getThen();
			
			return WhenStatement.newInstance(
				AndProposition.newInstance( getWhen().toPNF(), inner.getWhen() ),
				inner.getThen()
			).toWhenNF();
		} else if( getThen() instanceof Assignment ) { // when( phi, !p ) ==> ok
			return this;
			
		} else {
			throw new AssertionError();
		}
	}

	@Override
	public WhenStatement flatten() {
		Statement then = getThen().flatten();
		
		if( then instanceof WhenStatement ) { // when( phi, when(ksi,p) ) ==> when( and(phi,ksi), p )
			WhenStatement inner = (WhenStatement) then;
		
			WhenStatement result = WhenStatement.newInstance(
				AndProposition.newInstance( getWhen(), inner.getWhen() ).toSimplified(), // TODO .flatten(),
				inner.getThen()
			);
			
			return Helper.repeatUntilStable(result, WhenStatement::flatten);
		}
			
		return WhenStatement.newInstance(
			getWhen().toSimplified(), // TODO .flatten(),
			then
		);
	}

	@Override
	public Statement reduce() {
		Proposition when = getWhen().toSimplified();

		if( when.equals(BooleanLiteral.getFalse()) ) {
			return EmptyStatement.getInstance();
		}
		
		if( when.equals(BooleanLiteral.getTrue()) ) {
			return getThen().reduce();
		}
		
		return WhenStatement.newInstance(
			when,
			getThen().reduce()
		);
	}

	@Override
	public Statement emptify() {
		Proposition when = getWhen().toSimplified(); // TODO emptify()
		Statement then = getThen().emptify();
		
		if( getWhen().isEmpty() || getThen().isEmpty() ) {
			return EmptyStatement.getInstance();
		}
		
		return WhenStatement.newInstance(when, then);
	}

	/*
	 * TODO: when(p or q, e) ==> when(p, e) and when(q, e)
	 */
	
	@Override
	public Statement removeQuantifiers() {
		if( !contains(QuantifiedStatement.class) ) {
			return this;
		}
		
		return WhenStatement.newInstance(
			getWhen().removeQuantifiers(),
			getThen().removeQuantifiers()
		);
	}
	
	@Override
	protected Statement pullAndOverForAll() {
		return WhenStatement.newInstance(
			getWhen(),
			getThen().pullAndOverForAll()
		);
	}
	
	@Override
	protected Statement pullAndOverWhen() {
		//  when(phi, and(p,q)) ==> and(when(phi,p), when(phi,q))
//		Statement then = getThen().pullAndOverWhen();
		Statement then = Helper.repeatUntilStable(getThen(), Statement::pullAndOverWhen).simplify();
		
		if( then instanceof AndStatement ) {
			AndStatement and = (AndStatement) then;
			
			Statement result = AndStatement.newInstance(and.children()
				.map(e -> WhenStatement.newInstance(getWhen(), e))
			);
			
			return Helper.repeatUntilStable(result, Statement::pullAndOverWhen).simplify();
			
		}
		
		return WhenStatement.newInstance(getWhen(), then).simplify();
	}

	@Override
	protected Statement pushWhenOverForall() {
		// when(phi, forall(x,p)) ==> forall(x,when(phi,p))
//		Statement then = getThen().pushWhenOverForall();
		Statement then = Helper.repeatUntilStable(getThen(), Statement::pushWhenOverForall).simplify();
				
		if( then instanceof ForAllStatement ) {
			ForAllStatement forall = (ForAllStatement) then;
			
			Statement result =  ForAllStatement.newInstance(
				forall.variables(),
				WhenStatement.newInstance(getWhen(), forall.getChild())
			);
			
			return Helper.repeatUntilStable(result, Statement::pushWhenOverForall).simplify();

		}
		
		return WhenStatement.newInstance(getWhen(), then).simplify();
	}
	
	@Override
	protected Statement pushWhenOverOr() {
		// when(p or q,e) ==> when(p,e) and when(q,e)
		
		if( getWhen().uses(OrProposition.class) ) {
			OrProposition or = (OrProposition) getWhen().toDNF();

			Statement result =  AndStatement.newInstance(
				or.children()
					.map(e -> WhenStatement.newInstance(e, getThen().pushWhenOverOr()))
			);
			
			return Helper.repeatUntilStable(result, Statement::pushWhenOverOr).simplify();
			
		};
		
		return WhenStatement.newInstance(
			getWhen(), 
			getThen().pushWhenOverOr()
		).simplify();
	}
/*	@Override
	public WhenStatement toNegated() {
		return new WhenStatement(getWhen(), getThen().toNegated());
	}
*/
	
	@Override
	public boolean isGround() {
		return getWhen().isGround() && getThen().isGround();
	}

	@Override
	public WhenStatement ground(VariableSubstitution substitution) {
		return new WhenStatement(getWhen().ground(substitution), getThen().ground(substitution));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return freeVariables().collect(Collectors.toSet());
	}	
	@Override
	public Stream<Variable> freeVariables() {
		return Stream.concat(
			getWhen().freeVariables(),
			getThen().freeVariables()
		);
	}
	@Override
	public Set<Variable> getUniversallyQuantifiedVariables() {
		return universallyQuantifiedVariables().collect(Collectors.toSet());
	}
	
	@Override
	public Stream<Variable> universallyQuantifiedVariables() {
		return Stream.concat(
			getWhen().universallyQuantifiedVariables(),
			getThen().universallyQuantifiedVariables()
		);
	}
	@Override
	public Set<Variable> getExistentiallyQuantifiedVariables() {
		return getWhen().getExistentiallyQuantifiedVariables();
	}
	@Override
	public Stream<Variable> existentiallyQuantifiedVariables() {
		return getWhen().existentiallyQuantifiedVariables();
	}


	@Override
	public void accept(StatementVisitor visitor) {
        Objects.requireNonNull(visitor);
		if( visitor.enterWhenStatement(this) ) {
			children.forEach(e -> e.accept(visitor));
			visitor.leaveWhenStatement(this);
		}
	}
	
	@Override
	public String toString() {
		return "when(" + getWhen().toString() + "," + getThen().toString() + ")";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				getWhen(),
				getThen()
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

		WhenStatement other = (WhenStatement) obj;
		return Objects.equals(this.getWhen(), other.getWhen()) &&
			Objects.equals(this.getThen(), other.getThen());
	}
}
