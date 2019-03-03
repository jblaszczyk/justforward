package just.forward.normalization.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import just.forward.api.common.Predicate;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Literal;

@Immutable
public class GroundNormalizedRule {

	private static final java.util.function.Predicate<Literal> FILTER_POSITIVE = e -> !e.isNegated();
	private static final java.util.function.Predicate<Literal> FILTER_NEGATIVE = e -> e.isNegated();

	private final ImmutableList<Literal> body;
	private final NormalizedRule schematic;
	
	private GroundNormalizedRule(NormalizedRule schematic, Iterable<Literal> body) {
		Objects.requireNonNull(schematic);
		Objects.requireNonNull(body);
		this.schematic = schematic;
		this.body = ImmutableList.copyOf(body);
		
		if( this.body.stream().anyMatch(e -> !e.isGround()) ) {
			throw new IllegalArgumentException("All atoms in GroundNormalizedRule budy must be ground: " + body.toString());
		}
	}
	
	public static GroundNormalizedRule newInstance(NormalizedRule schematic, Iterable<Literal> body) {
		return new GroundNormalizedRule(schematic, body);
	}
	
	public static GroundNormalizedRule newInstance(NormalizedRule schematic, Stream<Literal> body) {
		return newInstance(schematic, body.collect(Collectors.toList()));
	}

	public static GroundNormalizedRule newInstance(NormalizedRule schematic, Literal... body) {
		return newInstance(schematic, Arrays.asList(body));
	}

	public NormalizedRule getSchematic() {
		return schematic;
	}
	
	public List<Literal> getPositiveBody() {
		return positiveBody().collect(Collectors.toList());
	}
	
	public List<Literal> getNegativeBody() {
		return negativeBody().collect(Collectors.toList());
	}
	
	public List<Literal> getBody() {
		return body;
	}

	public Stream<Literal> positiveBody() {
		return body.stream()
			.filter(FILTER_POSITIVE);
	}
	public Stream<Literal> negativeBody() {
		return body.stream()
			.filter(FILTER_NEGATIVE);
	}
	
	public Stream<Literal> body() {
		return body.stream();
	}

	Stream<Predicate> predicatesInBody() {
		return body()
			.filter( e -> e instanceof AtomLiteral)
			.map( e -> ((AtomLiteral) e).getPredicate() );
	}

	@Override
	public String toString() {
		return getSchematic().getPredicate().toString() + " :- " + Joiner.on(',').join(getBody());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				schematic,
				body
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		GroundNormalizedRule other = (GroundNormalizedRule) obj;
		return Objects.equals(this.schematic, other.schematic) &&
				Objects.equals(this.body, other.body);
	}

}
