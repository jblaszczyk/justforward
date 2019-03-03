package just.forward.api.proposition;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.Helper;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.expression.Variable;

public abstract class AbstractCompoundProposition extends Proposition {
	
	protected ImmutableSet<Proposition> children;

	protected AbstractCompoundProposition(Iterable<? extends Proposition> children) {
		Objects.requireNonNull(children);
		this.children = ImmutableSet.copyOf( Helper.cleanPropositions(children) );
	}
	
	public Set<Proposition> getChildren() {
		return children;
	}
	public Stream<Proposition> children() {
		return getChildren().stream(); 
	}	
	
	@Override
	public boolean isGround() {
		return children().allMatch(Proposition::isGround);
	}
	
	protected List<Proposition> groundChildren(VariableSubstitution subsitution) {
		return children().map(e -> e.ground(subsitution)).collect(Collectors.toList());
	}	
	
	@Override
	public boolean isNNF() {
		return children().allMatch(Proposition::isNNF);
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty();
	}
	
	protected boolean directlyComplementsEachOther(List<Proposition> items) {
		for(int i = 0; i < items.size(); i++) {
			for(int j = i + 1; j < items.size(); j++) {
				if( items.get(i).isDirectComplementOf(items.get(j)) ) {
					return true;
				}
			}
		}
		
		return false;
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
