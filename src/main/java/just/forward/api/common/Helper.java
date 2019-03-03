package just.forward.api.common;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import just.forward.api.proposition.EmptyProposition;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.EmptyStatement;
import just.forward.api.statement.Statement;

public class Helper {

	private Helper() {}
	
	public final static <T extends Proposition> List<T> cleanPropositions(Iterable<T> items) {
		return StreamSupport.stream(items.spliterator(), false)
				.filter(EmptyProposition.filterOut())
				.collect(Collectors.toList());
	}
	
	public final static <T extends Statement> List<T> cleanStatements(Iterable<T> items) {
		return StreamSupport.stream(items.spliterator(), false)
				.filter(EmptyStatement.filterOut())
				.collect(Collectors.toList());
	}
	
	public final static <T> T repeatUntilStable(T arg, Function<T,T> func) {
		T prev = arg;
		T current = func.apply(arg);
		while( /* TODO perf: prev.hashCode() != current.hashCode || */ !prev.equals(current) ) {
			prev = current;
			current = func.apply(current);
		}
		
		return current;
	}
	

}
