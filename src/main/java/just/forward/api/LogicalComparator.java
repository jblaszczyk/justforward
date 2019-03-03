package just.forward.api;

import static just.forward.api.common.Sentences.falseValue;
import static just.forward.api.common.Sentences.trueValue;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import just.forward.api.common.Substitution;
import just.forward.api.proposition.AbstractCompoundProposition;
import just.forward.api.proposition.Literal;
import just.forward.api.proposition.Proposition;

public class LogicalComparator {

	private static final LogicalComparator INSTANCE = new LogicalComparator();
	
	protected LogicalComparator() {}
	
	public static LogicalComparator newInstance() {
		return INSTANCE;
	}
	
	public boolean compare(Proposition arg1, Proposition arg2) {
		Set<Literal> atoms1 = Sets.newHashSet();
		Set<Literal> atoms2 = Sets.newHashSet();
		collectLiterals(atoms1, arg1);
		collectLiterals(atoms2, arg2);

		List<Literal> atomsToSubstitute = Lists.newArrayList(atoms1);
		atomsToSubstitute.retainAll(atoms2);
		atomsToSubstitute.remove(trueValue());
		atomsToSubstitute.remove(falseValue());
		
		long combinations = 2 << (atomsToSubstitute.size() - 1);
		
		if( combinations == 0 ) {
			Proposition result1 = arg1.toSimplified(); 
			Proposition result2 = arg2.toSimplified();
			
			return compare0(result1, result2);
		}
		
		for( long i = 0; i < combinations; i++ ) {
			Substitution map = createSubstitution(atomsToSubstitute, i);
			Proposition result1 = arg1.substitute(map).toSimplified(); 
			Proposition result2 = arg2.substitute(map).toSimplified();
			
			if( !compare0(result1, result2) ) {
				return false;
			}
		}
		
		return true;
	}

	private boolean compare0(Proposition arg1, Proposition arg2) {
/*		if( !trueValue().equals(arg1) && !falseValue().equals(arg1)) {
			return false;
		}

		if( !trueValue().equals(arg2) && !falseValue().equals(arg2)) {
			return false;
		}
*/		
		return arg1.equals(arg2);
	}
	
	Substitution createSubstitution(List<Literal> atoms, long combinationNo) {
		Substitution.Builder substitution = Substitution.builder();
		for(int i = 0; i < atoms.size(); i++ ) {
			long value = (combinationNo >> i) % 2;
			substitution.map(atoms.get(i), ( value == 1 )?( trueValue() ):( falseValue() ) );
		}
		
		return substitution.build();
	}
	
	void collectLiterals(Set<Literal> atoms, Proposition sentence) {
		if( sentence instanceof Literal ) {
			collectLiterals(atoms, (Literal) sentence);
			
		} else if( sentence instanceof AbstractCompoundProposition ) {
			collectLiterals(atoms, (AbstractCompoundProposition) sentence);
			
		} else {
			throw new AssertionError();
		}
	}
	
	void collectLiterals(Set<Literal> atoms, Literal atom) {
		atoms.add(atom);
	}

	void collectLiterals(Set<Literal> atoms, AbstractCompoundProposition sentence) {
		sentence.children().forEach( e -> collectLiterals(atoms, e) );
	}
}
