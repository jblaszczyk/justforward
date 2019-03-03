package just.forward.api.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import just.forward.util.ImmutableCollectors;

@Immutable
public final class Namespace {

	public static final char DELIMITER = '.';
	private static final Namespace DEFAULT = syntheticOf("_");
	private static final Namespace STANDARD = syntheticOf("std");
	private static final Namespace HIDDEN = syntheticOf("\u2205");
	
	private final boolean isSynthtic;
	private final ImmutableList<String> segments;
	
	private Namespace(boolean isSynthtic, Iterable<String> segments) {
		this.isSynthtic = isSynthtic;
		this.segments = Lists.newArrayList(segments).stream()
			.flatMap(e -> Stream.of(e.split("\\.")) )
			.collect(ImmutableCollectors.toList());
	}
	
	public static Namespace of(Iterable<String> segments) {
		return new Namespace(false, segments);
	}

	public static Namespace of(String... segments) {
		return new Namespace(false, Arrays.asList(segments));
	}

	public static Namespace of(Stream<String> segments) {
		return new Namespace(false, segments.collect(Collectors.toList()));
	}

	public static Namespace syntheticOf(Iterable<String> segments) {
		return new Namespace(true, segments);
	}

	public static Namespace syntheticOf(String... segments) {
		return new Namespace(true, Arrays.asList(segments));
	}

	public static Namespace syntheticOf(Stream<String> segments) {
		return new Namespace(true, segments.collect(Collectors.toList()));
	}

	public static Namespace getDefault() {
		return DEFAULT;
	}
	public static Namespace getStandard() {
		return STANDARD;
	}
	public static Namespace getHidden() {
		return HIDDEN;
	}
	
	public ImmutableList<String> getSegments() {
		return segments;
	}
	
	public Stream<String> segments() {
		return segments.stream();
	}
	
	public boolean isSynthtic() {
		return isSynthtic;
	}
	
	@Override
	public String toString() {
		return Joiner.on(DELIMITER).join(segments);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		Namespace other = (Namespace) obj;
		
		return Objects.equals(this.segments, other.segments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				segments
		);
	}
}
