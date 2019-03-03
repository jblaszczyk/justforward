package just.forward.api.proposition;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import just.forward.api.expression.Variable;

public class PropositionPNFConverter {

	private static final PropositionPNFConverter INSTANCE = new PropositionPNFConverter();
	private PropositionPNFConverter() {}
	
	public static PropositionPNFConverter newInstance() {
		return INSTANCE;
	}
	
	public Proposition convert(Proposition sentence) {
		List<QuantifiedVariable> quantifiers = Lists.newLinkedList();
		Proposition result = process(sentence, quantifiers);
		
		quantifiers = merge(quantifiers);
		
		for(QuantifiedVariable current : quantifiers) {
			if( current.type == QuantifiedType.FORALL ) {
				result = ForAllProposition.newInstance(current.vars, result);
			} else {
				result = ExistsProposition.newInstance(current.vars, result);
			}
		}
		
		return result;
	}
	
	List<QuantifiedVariable> merge(List<QuantifiedVariable> input) {
		List<QuantifiedVariable> result = Lists.newLinkedList();
		if( input.isEmpty() ) {
			return result;
		}
		
		Iterator<QuantifiedVariable> it = input.iterator();
		QuantifiedVariable current = it.next();
		result.add(current);
		
		while(it.hasNext()) {
			QuantifiedVariable next = it.next();
			if( next.type == current.type ) {
				current.tryMerge(next);
			} else {
				current = next;
				result.add(next);
			}
		}
		
		return result;
	}

	Proposition process(Proposition sentence, List<QuantifiedVariable> quantifiers) {
		if( sentence instanceof NotProposition ) {
			return processNot((NotProposition) sentence, quantifiers);
			
		} else if( sentence instanceof Literal ) {
			return processSimpleProposition((Literal) sentence, quantifiers);
			
		} else if( sentence instanceof ForAllProposition ) {
			return processQuantifier((ForAllProposition) sentence, quantifiers, QuantifiedType.FORALL);
			
		} else if( sentence instanceof ExistsProposition ) {
			return processQuantifier((ExistsProposition) sentence, quantifiers, QuantifiedType.EXISTS);
			
		} else if( sentence instanceof AndProposition ) {
			return processAnd((AndProposition) sentence, quantifiers);
			
		} else if( sentence instanceof OrProposition ) {
			return processOr((OrProposition) sentence, quantifiers);

		} else {
			throw new RuntimeException( new IllegalArgumentException("class " + sentence.getClass() + " is not supported") );
		}
	}

	private Proposition processNot(NotProposition sentence, List<QuantifiedVariable> quantifiers) {
		List<QuantifiedVariable> childQuantifiers = Lists.newLinkedList();
		Proposition child = process(sentence.getChild(), childQuantifiers);
		flip(childQuantifiers);
		quantifiers.addAll(childQuantifiers);
		
		return NotProposition.of(child);
	}

	private Proposition processAnd(AndProposition sentence, List<QuantifiedVariable> quantifiers) {
		List<Proposition> newChildren = Lists.newLinkedList();
		for( Proposition child : sentence.getChildren() ) {
			List<QuantifiedVariable> childQuantifiers = Lists.newLinkedList();
			newChildren.add(process(child, childQuantifiers));
			quantifiers.addAll(childQuantifiers);
		}

		return AndProposition.newInstance(newChildren);
	}

	private Proposition processOr(OrProposition sentence, List<QuantifiedVariable> quantifiers) {
		List<Proposition> newChildren = Lists.newLinkedList();
		for( Proposition child : sentence.getChildren() ) {
			List<QuantifiedVariable> childQuantifiers = Lists.newLinkedList();
			newChildren.add(process(child, childQuantifiers));
			quantifiers.addAll(childQuantifiers);
		}

		return OrProposition.newInstance(newChildren);
	}

	private Proposition processQuantifier(QuantifiedProposition sentence, List<QuantifiedVariable> quantifiers, QuantifiedType type) {
		List<QuantifiedVariable> childQuantifiers = Lists.newLinkedList();
		Proposition result = process(sentence.getChild(), childQuantifiers); 
		quantifiers.addAll(childQuantifiers);
		quantifiers.add( new QuantifiedVariable(sentence.getVariables(), type) );
		return result;
	}

	private Proposition processSimpleProposition(Literal sentence, List<QuantifiedVariable> quantifiers) {
		return sentence;
	}

	void flip(List<QuantifiedVariable> quantifiers) {
		quantifiers.forEach(e -> e.flip());
	}
	
	private enum QuantifiedType {
		FORALL,
		EXISTS
	}
	
	private static class QuantifiedVariable {
		Set<Variable> vars;
		QuantifiedType type;
		
		public QuantifiedVariable(Iterable<Variable> vars, QuantifiedType type) {
			Objects.requireNonNull(vars);
			Objects.requireNonNull(type);
			this.vars = Sets.newHashSet(vars);
			this.type = type;
		}
		
		public void flip() {
			type = (type == QuantifiedType.FORALL)?(QuantifiedType.EXISTS):(QuantifiedType.FORALL);
		}
		
		public boolean tryMerge(QuantifiedVariable other) {
			if( type != other.type ) {
				return false;
			}
			
			vars.addAll(other.vars);
			return true;
		}
	}
}
