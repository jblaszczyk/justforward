package just.forward.api.expression;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.atom;
import static just.forward.api.common.Sentences.constant;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.or;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import just.forward.api.expression.Constant;
import just.forward.api.expression.VariableSubstitution;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Proposition;
import just.forward.test.JustTest;

public class SubstitutionTest extends JustTest {

	public static AtomLiteral rz = atom(R, z);
	public static AtomLiteral sz = atom(S, z);
	public static Constant obj1 = constant("obj1");
	public static Constant obj2 = constant("obj2");

	@Test
	public void trivial() throws Exception {
		
		Proposition input = 
				py;
		
		Proposition expected = 
				atom(P, obj1);

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
			.build();
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "p(obj1)" );
	}

	@Test
	public void negation() throws Exception {
		
		Proposition input = 
				not(py);
		
		Proposition expected = 
				not(atom(P, obj1));

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
		.build();
		
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "!p(obj1)" );
	}
	
	@Test
	public void conjunction() throws Exception {
		
		Proposition input = 
				and(py, rz);
		
		Proposition expected = 
				and(
						atom(P, obj1),
						atom(R, obj2)
				);

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
			.map(z, obj2)
		.build();
		
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "and(p(obj1),r(obj2))" );
	}	

	@Test
	public void disjunction() throws Exception {
		
		Proposition input = 
				or(py, rz);
		
		Proposition expected = 
				or(
						atom(P, obj1),
						atom(R, obj2)
				);

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
			.map(z, obj2)
		.build();
		
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "or(p(obj1),r(obj2))" );
	}
	

	
	@Test
	public void cnf() throws Exception {
		
		Proposition input = 
				and( or(py, rz), or(qy, sz) );
		
		Proposition expected = 
				and(
						or(
								atom(P, obj1),
								atom(R, obj2)
						), or(
								atom(Q, obj1),
								atom(S, obj2)
						)
				);

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
			.map(z, obj2)
		.build();
		
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "and(or(p(obj1),r(obj2)),or(q(obj1),s(obj2)))" );
	}	

	@Test
	public void dnf() throws Exception {
		
		Proposition input = 
				or( and(py, rz), and(qy, sz) );
		
		Proposition expected = 
				or(
						and(
								atom(P, obj1),
								atom(R, obj2)
						), and(
								atom(Q, obj1),
								atom(S, obj2)
						)
				);

		VariableSubstitution subsitution = VariableSubstitution.builder()
			.map(y, obj1)
			.map(z, obj2)
		.build();
	
		assertThat( input.ground(subsitution) ).isEqualTo( expected );
		assertThat( input.ground(subsitution).toString() ).isEqualTo( "or(and(p(obj1),r(obj2)),and(q(obj1),s(obj2)))" );
	}		
	
}
