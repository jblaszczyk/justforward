package just.forward.sas.search;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import just.forward.sas.model.SASAction;
import just.forward.sas.model.SASProblem;

public class SASPlanner {
	

	SASProblem problem;
	AStarSASStrategy strategy;
	List<SASAction> path;
	Set<SASSearchNode> visitedSeachNodes = Sets.newHashSet();
	NaiveSASSearchNodeSuccessorGenerator generator;
	
	SASPlanner(SASProblem problem, AStarSASStrategy strategy, NaiveSASSearchNodeSuccessorGenerator generator) {
        Objects.requireNonNull(problem);
        Objects.requireNonNull(strategy);
        Objects.requireNonNull(generator);
		this.problem = problem;
		this.strategy = strategy;
		this.generator = generator;
	}
	
	public static SASPlanner newInstance(SASProblem problem, AStarSASStrategy strategy, NaiveSASSearchNodeSuccessorGenerator generator) {
		return new SASPlanner(problem, strategy, generator);
	}
	
	public boolean hasNextStep() {
		return !strategy.isDeadend();
	}

	public boolean nextStep() {
		SASSearchNode node = strategy.popNode();
		visitedSeachNodes.add(node);
		
		if( node.entailsAll(problem.getGoal()) ) {
			path = ImmutableList.copyOf( node.getPath() );
			return true;
		}
		
		for(SASSearchNode newNode : generator.successors( node )) {
			if( !visitedSeachNodes.contains(newNode)  ) {
				strategy.addNode( newNode );
			}
		}

		return false;
	}
	
	public List<SASAction> plan() {
		strategy.addNode( SASSearchNode.of( problem.getInit() ) );
		while( hasNextStep() ) {
			if( nextStep() ) {
				return getPlan();
			}
		}
		
		return Collections.emptyList();
	}
	
	public List<SASAction> getPlan() {
		return path;
	}
	
}
