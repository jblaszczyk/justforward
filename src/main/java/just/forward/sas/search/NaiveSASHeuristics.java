package just.forward.sas.search;

import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import just.forward.api.proposition.Literal;
import just.forward.sas.model.SASProblem;
import just.forward.sas.model.SASVariable;
import just.forward.search.Heuristics;

// open world
@Immutable
public class NaiveSASHeuristics implements Heuristics<SASSearchNode> {

	public static double EPSILON = 0.00000001; // TODO
	
	SASProblem problem;
	
	NaiveSASHeuristics(SASProblem problem) {
		Objects.requireNonNull(problem);
		this.problem = problem;
	}
	
	public static NaiveSASHeuristics newInstance(SASProblem problem) {
		return new NaiveSASHeuristics(problem);
	}
	
	public int goalDistance(SASSearchNode node) {
		int unachieved = 0;
		
		for(SASVariable var : problem.getGoal().getVariables()) {
			Literal goalValue = problem.getGoal().findValue(var);
			
			if( !node.entails(var, goalValue) ) {
				unachieved++;
			}
		}
		
		return unachieved;
	}
	}
