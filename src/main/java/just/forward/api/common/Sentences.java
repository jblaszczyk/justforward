package just.forward.api.common;

import java.util.Arrays;

import just.forward.api.expression.Constant;
import just.forward.api.expression.FluentTerm;
import just.forward.api.expression.Term;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.AndProposition;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.BooleanLiteral;
import just.forward.api.proposition.EmptyProposition;
import just.forward.api.proposition.ExistsProposition;
import just.forward.api.proposition.ForAllProposition;
import just.forward.api.proposition.NotProposition;
import just.forward.api.proposition.OrProposition;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.AndStatement;
import just.forward.api.statement.BooleanAssignment;
import just.forward.api.statement.EmptyStatement;
import just.forward.api.statement.FluentAssignment;
import just.forward.api.statement.ForAllStatement;
import just.forward.api.statement.Statement;
import just.forward.api.statement.WhenStatement;

public class Sentences {

	private Sentences() {}

	public static Predicate predicate(String name, Variable... parameters) {
		return Predicate.newInstance(name, parameters);
	}
	
	public static Fluent fluent(String name, Variable... parameters) {
		return Fluent.newInstance(name, parameters);
	}

	public static Variable var(String name) {
		return Variable.newInstance(name);
	}
	
	public static Constant constant(String name) {
		return Constant.newInstance(name);
	}

	public static BooleanLiteral trueValue() {
		return BooleanLiteral.getTrue();
	}

	public static BooleanLiteral falseValue() {
		return BooleanLiteral.getFalse();
	}
	
	public static AtomLiteral atom(Predicate p, Term... args) {
		return AtomLiteral.newInstance(p, args);
	}
	
	public static AndProposition and(Proposition... sentences) {
		return AndProposition.newInstance(sentences);
	}
	
	public static OrProposition or(Proposition... sentences) {
		return OrProposition.newInstance(sentences);
	}
	
	public static AtomLiteral not(AtomLiteral atom) {
		return atom.toNegated();
	}

	public static NotProposition not(Proposition sentence) {
		return NotProposition.of(sentence);
	}

	public static ForAllProposition forall(Proposition sentence, Variable... variables) {
		return ForAllProposition.newInstance( Arrays.asList(variables), sentence);
	}

	public static ExistsProposition exists(Proposition sentence, Variable... variables) {
		return ExistsProposition.newInstance( Arrays.asList(variables), sentence);
	}

	public static ForAllProposition forall(Variable variable, Proposition sentence) {
		return ForAllProposition.newInstance( Arrays.asList(variable), sentence);
	}

	public static ExistsProposition exists(Variable variable, Proposition sentence) {
		return ExistsProposition.newInstance( Arrays.asList(variable), sentence);
	}

	public static AndStatement and(Statement... sentences) {
		return AndStatement.newInstance(sentences);
	}
	
	public static ForAllStatement forall(Statement sentence, Variable... variables) {
		return ForAllStatement.newInstance( Arrays.asList(variables), sentence);
	}
	public static ForAllStatement forall(Variable variable, Statement sentence) {
		return ForAllStatement.newInstance( Arrays.asList(variable), sentence);
	}
	
	public static WhenStatement when(Proposition when, Statement then) {
		return WhenStatement.newInstance(when, then);
	}
	
	public static Statement then(Statement statement) {
		return statement;
	}
	
	public static BooleanAssignment then(AtomLiteral atom) {
		return BooleanAssignment.of(atom);
	}
	
	public static FluentAssignment then(FluentTerm fluentTerm, Term value) {
		return FluentAssignment.newInstance(fluentTerm, value);
	}
	
	public static FluentTerm f(Fluent fluent, Term... args) {
		return FluentTerm.newInstance(fluent, args);
	}
	
	public static AtomLiteral eq(Term a, Term b) {
		return atom(predicate("eq", var("a"), var("b")), a, b); // TODO
	}
	
	public static EmptyProposition emptyProposition() {
		return EmptyProposition.getInstance();
	}

	public static EmptyStatement emptyStatement() {
		return EmptyStatement.getInstance();
	}

	@Deprecated
	@SuppressWarnings("unused")
	private static void example1() {
		Variable x = var("x"), y = var("y"), z = var("z");
		Predicate P = predicate("p", var("x")), Q = predicate("q", var("x")), R = predicate("r", var("x")), S = predicate("s", var("x"));
		AtomLiteral px = atom(P, x), qx = atom(Q, x), rx = atom(R, x), sx = atom(S, x);
		AtomLiteral py = atom(P, y), qy = atom(Q, y), ry = atom(R, y), sy = atom(S, y);
		AtomLiteral pz = atom(P, z), qz = atom(Q, z), rz = atom(R, z), sz = atom(S, z);
		Fluent f = fluent("f", var("x"));
		
		Proposition precondition1 = not(and(px, not(qx)));
		Statement effect = and( then(px), then(not(qx)) );

		Proposition preconditionWithFluent = and(eq(f(f,x),y));
		Statement effectWithFluent = and( then(f(f,x),y), then(not(qx)) );

	
	}
}
