package just.forward.sas.search;

import java.util.Objects;
import java.util.PriorityQueue;

import just.forward.search.Heuristics;
import just.forward.search.SearchStrategy;

public class AStarSASStrategy implements SearchStrategy<SASSearchNode> {

	PriorityQueue<SASSearchNode> queue ;
	Heuristics<SASSearchNode> heuristics;
	
	AStarSASStrategy(Heuristics<SASSearchNode> heuristics) {
		Objects.requireNonNull(heuristics);
		this.heuristics = heuristics;
		queue = new PriorityQueue<SASSearchNode>( SASHeuristicsComparator.newInstance(heuristics) ); 
	}
	
	public static AStarSASStrategy newInstance(Heuristics<SASSearchNode> heuristics) {
		return new AStarSASStrategy(heuristics);
		
	}
	public void addNode(SASSearchNode node) {
		queue.add(node);
	}
	
	public SASSearchNode popNode() {
		return queue.poll();
	}
	
	public boolean isDeadend() {
		return queue.isEmpty();
	}
}
