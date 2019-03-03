package just.forward.sas.search;

import java.util.Objects;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.Sets;

import just.forward.sas.model.SASAction;
import just.forward.sas.model.SASProblem;
import just.forward.search.SearchNodeSuccessorGenerator;

@Immutable
public class NaiveSASSearchNodeSuccessorGenerator implements SearchNodeSuccessorGenerator<SASSearchNode> {

	SASProblem problem;
	
	NaiveSASSearchNodeSuccessorGenerator(SASProblem problem) {
		Objects.requireNonNull(problem);
		this.problem = problem;
	}
	
	public static NaiveSASSearchNodeSuccessorGenerator newInstance(SASProblem problem) {
		return new NaiveSASSearchNodeSuccessorGenerator(problem);
	}
	
	public Set<SASSearchNode> successors(SASSearchNode node) {
		Set<SASSearchNode> result = Sets.newHashSet();
		
		for(SASAction action : problem.getDomain().getActions()) {
			if( node.isApplicable(action) ) {
				result.add( node.apply(action) );
			}
		}

		return result;
	}
}
