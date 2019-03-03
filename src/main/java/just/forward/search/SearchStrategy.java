package just.forward.search;

public interface SearchStrategy<T> {
	public void addNode(T node);
	
	public T popNode();
	
	public boolean isDeadend();

}
