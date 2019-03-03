package just.forward.sas.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.proposition.Literal;

/* SASFiniteDomain */
@Immutable
public class SASVariableDomain {

	ImmutableSet<Literal> coreDomain;

	SASVariableDomain(Iterable<Literal> domain) {
		Objects.requireNonNull(domain);
		
		this.coreDomain = ImmutableSet.copyOf(domain);
//		result.add(SASValue.NEITHER); TODO
//		result.add(SASValue.DONT_CARE); TODO
	}

	public static Builder builder() {
		return new Builder();
	}

	public boolean verifyValue(Literal currentValue) {
		Objects.requireNonNull(currentValue);
		if( !coreDomain.contains(currentValue) ) {
			throw new RuntimeException("Non-domain value set to a variable.");
		}
		
		return true;
	}
	
	public Set<Literal> getDomain() {
		return coreDomain; 
	}

	public Stream<Literal> domain() {
		return getDomain().stream();
	}
	
	public static class Builder {
		LinkedList<Literal> domain = Lists.newLinkedList();
		
		Builder() {}
		
		public Builder atom(Literal element) {
			Objects.requireNonNull(element);
			domain.add(element);
			return this;
		}
		
		public Builder atoms(Iterable<Literal> elements) {
			Objects.requireNonNull(elements);
			Iterables.addAll( this.domain, elements );
			return this;
		}
		
		public Builder atoms(Stream<Literal> elements) {
			Objects.requireNonNull(elements);
			this.domain.addAll( elements.collect( Collectors.toList() ));
			return this;
		}

		public Builder atoms(Literal... elements) {
			Objects.requireNonNull(elements);
			Iterables.addAll( this.domain, Arrays.asList(elements) );
			return this;
		}	
		
		public SASVariableDomain build() {
			return new SASVariableDomain(domain);
		}
	}
	@Override
	public String toString() {
		return "{" + Joiner.on(",").join(coreDomain) + "}";
	}
}
