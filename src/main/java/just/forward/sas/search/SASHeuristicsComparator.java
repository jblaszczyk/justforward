package just.forward.sas.search;

import java.util.Comparator;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import just.forward.search.Heuristics;

@Immutable
public class SASHeuristicsComparator implements Comparator<SASSearchNode> {

		Heuristics<SASSearchNode> heuristics;
		
		SASHeuristicsComparator(Heuristics<SASSearchNode> heuristics) {
			Objects.requireNonNull(heuristics);
			this.heuristics = heuristics;
		}
		
		public static SASHeuristicsComparator newInstance(Heuristics<SASSearchNode> heuristics) {
			return new SASHeuristicsComparator(heuristics);
		}
		
		@Override
		public int compare(SASSearchNode e1, SASSearchNode e2) {
			return heuristics.goalDistance(e1) - heuristics.goalDistance(e2);
		}
		
	}
