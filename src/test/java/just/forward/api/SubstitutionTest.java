package just.forward.api;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.exists;
import static just.forward.api.common.Sentences.forall;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.or;
import static just.forward.api.common.Sentences.var;
import static just.forward.api.common.Sentences.when;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

import just.forward.api.common.Substitution;
import just.forward.api.expression.Variable;
import just.forward.api.proposition.Proposition;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class SubstitutionTest extends JustTest {
	
	@Test
	public void testTrivial() throws Exception {
		given:;
			Proposition input =
				p;
			
			Proposition expected =
				q;
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testNoMatch() throws Exception {
		given:;
			Proposition input =
				p;
			
			Proposition expected =
				p;
			
			Substitution substitution = Substitution.builder()
				.map(q, r)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testNot() throws Exception {
		given:;
			Proposition input =
				not(p);
			
			Proposition expected =
				not(q);
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
				.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testNot2() throws Exception {
		given:;
			Proposition input =
				not(p);
			
			Proposition expected =
				p;
			
			Substitution substitution = Substitution.builder()
				.map(p, not(p))
				.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void testNot3() throws Exception {
		given:;
			Proposition input =
				not(p);
			
			Proposition expected =
				p;
			
			Substitution substitution = Substitution.builder()
				.map(not(p), p)
				.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void testNot4() throws Exception {
		given:;
			Proposition input =
				not(p);
			
			Proposition expected =
				q;
			
			Substitution substitution = Substitution.builder()
				.map(p, not(q))
				.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void testAnd() throws Exception {
		given:;
			Proposition input =
				and(p, s, t);
			
			Proposition expected =
				and(q, s, t);
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
			.build();

		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testOr() throws Exception {
		given:;
			Proposition input =
				or(p, s, t);
			
			Proposition expected =
				or(q, s, t);
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void testComplex1() throws Exception {
		given:;
			Proposition input =
				and(p, or(r, s));
			
			Proposition expected =
				and(q, or(r, s));
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void testComplex2() throws Exception {
		given:;
			Proposition input =
				and(p, or(r, s));
			
			Proposition expected =
				and(p, q);
			
			Substitution substitution = Substitution.builder()
				.map(or(r, s), q)
			.build();

		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void existsSimple() throws Exception {
		given:;
			Proposition input =
				and(p, exists(y, r));
			
			Proposition expected =
				and(p, exists(s));
			
			Substitution substitution = Substitution.builder()
				.map(r, s)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void forallSimple() throws Exception {
		given:;
			Proposition input =
				and(p, forall(y, r));
			
			Proposition expected =
				and(p, forall(y, s));
			
			Substitution substitution = Substitution.builder()
				.map(r, s)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void existsSwap() throws Exception {
		given:;
			Proposition input =
				and(p, exists(y, r));
			
			Proposition expected =
				and(p, forall(y, s));
			
			Substitution substitution = Substitution.builder()
				.map(exists(y, r), forall(y, s))
			.build();
				
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void forallSwap() throws Exception {
		given:;
			Proposition input =
				and(p, forall(y, r));
			
			Proposition expected =
				and(p, exists(y, s));
			
			Substitution substitution = Substitution.builder()
				.map(forall(y, r), exists(y, s))
			.build();
				
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	/*
	@Test
	public void testWhen0() throws Exception {
		given:;
			Proposition input =
				and(p, when(s, t));
			
			Proposition expected =
				and(q, when(s, t));
			
			Substitution substitution = Substitution.builder()
				.map(p, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testWhen1() throws Exception {
		given:;
			Proposition input =
				and(p, when(s, t));
			
			Proposition expected =
				and(p, when(q, t));
			
			Substitution substitution = Substitution.builder()
				.map(s, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void testWhen2() throws Exception {
		given:;
			Proposition input =
				and(p, when(s, t));
			
			Proposition expected =
				and(p, when(s, q));
			
			Substitution substitution = Substitution.builder()
				.map(t, q)
			.build();
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void swapWhen() throws Exception {
		given:;
			Proposition input =
				and(p, when(s, t));
			
			Proposition expected =
				and(p, q);
			
			Substitution substitution = Substitution.builder()
				.map(when(s, t), q)
			
		when:;
			Proposition result = input.substitute(substitution);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
*/	
}
