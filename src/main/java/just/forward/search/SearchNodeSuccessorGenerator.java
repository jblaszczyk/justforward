package just.forward.search;

import java.util.Set;

public interface SearchNodeSuccessorGenerator<T> {
	public Set<T> successors(T node);
}
