package just.forward.api.proposition;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import just.forward.api.common.Substitution;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.TermVisitor;
import just.forward.api.expression.Variable;
import just.forward.api.statement.Statement;

public abstract class Proposition {

	protected Proposition() {}
	
	public abstract boolean isEmpty();
	public abstract boolean isNNF();
	public abstract boolean isPNF();
	public abstract boolean isDNF();
	
	public abstract Proposition toSimplified();
	public abstract Proposition toNegated();
	public abstract Proposition toNNF();
	public abstract Proposition toDNF();
	public Proposition toPNF() {
		if( isPNF() ) {
			return this;
		}
		
		return PropositionPNFConverter.newInstance().convert(this);
	}
	
	// all variables = free vars + forall vars + exists vars
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

	public abstract Proposition removeQuantifiers();

	public abstract boolean isGround();
	public abstract Proposition ground(VariableSubstitution substitution);
	public abstract Proposition substitute(Substitution substitution);
	
	public abstract boolean contains(Class<?> clazz);
	public abstract boolean uses(Class<?> clazz);

	public boolean isDirectComplementOf(Proposition other) {
		return this.toNegated().equals(other);
	}
	
	public abstract void accept(PropositionVisitor visitor);

	
/*	TODO:
	isSimplified()
	ImplicationSentence, IffSentence, XorSentence
 */
	
}
