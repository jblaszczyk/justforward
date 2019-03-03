package just.forward.test;

import static just.forward.api.common.Sentences.atom;
import static just.forward.api.common.Sentences.predicate;
import static just.forward.api.common.Sentences.var;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import just.forward.api.common.Predicate;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.AtomLiteral;

public class JustTest {
	
	public static final Variable X = var("X");
	public static final Predicate P = predicate("p", X);
	public static final Predicate Q = predicate("q", X);
	public static final Predicate R = predicate("r", X);
	public static final Predicate S = predicate("s", X);
	public static final Predicate T = predicate("t", X);
	public static final Predicate U = predicate("u", X);
	
	public static final Variable x = var("x");
	public static final Variable y = var("y");
	public static final Variable z = var("z");
	public static final Variable w = var("w");

	public static final AtomLiteral p = atom(P, x);
	public static final AtomLiteral q = atom(Q, x);
	public static final AtomLiteral r = atom(R, x);
	public static final AtomLiteral s = atom(S, x);
	public static final AtomLiteral t = atom(T, x);
	public static final AtomLiteral u = atom(U, x);

	public static final AtomLiteral px = atom(P, x);
	public static final AtomLiteral qx = atom(Q, x);
	public static final AtomLiteral rx = atom(R, x);
	public static final AtomLiteral sx = atom(S, x);
	public static final AtomLiteral tx = atom(T, x);
	public static final AtomLiteral ux = atom(U, x);

	public static final AtomLiteral py = atom(P, y);
	public static final AtomLiteral qy = atom(Q, y);
	public static final AtomLiteral ry = atom(R, y);
	public static final AtomLiteral sy = atom(S, y);
	public static final AtomLiteral ty = atom(T, y);
	public static final AtomLiteral uy = atom(U, y);
	
	public static final AtomLiteral pz = atom(P, z);
	public static final AtomLiteral qz = atom(Q, z);
	public static final AtomLiteral rz = atom(R, z);
	public static final AtomLiteral sz = atom(S, z);
	public static final AtomLiteral tz = atom(T, z);
	public static final AtomLiteral uz = atom(U, z);

	protected static <T> LinkedList<T> toList(Iterable<T> elements) {
		return Lists.newLinkedList(elements);
	}

	protected static <T> LinkedList<T> toList(Stream<T> elements) {
		return Lists.newLinkedList(elements.collect(Collectors.toList()));
	}
}
