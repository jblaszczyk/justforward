package just.forward.api.statement;

import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Test;

import just.forward.test.JustTest;
/*
@SuppressWarnings("unused")
public class StatementNNFTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Statement input = 
				then(p);
		
			Statement expected = 
				then(p);

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void negation() throws Exception {
		given:;
			Statement input = 
				then(not(p));
		
			Statement expected = 
				then(p.toNegated());

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void doubleNegation() throws Exception {
		given:;
			Statement input = 
				then(not(not(p)));
		
			Statement expected = 
				then(p);

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void tripleNegation() throws Exception {
		given:;
			Statement input = 
				then(not(not(not(p))));
		
			Statement expected = 
				then(not(p));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void recursiveConjunction() throws Exception {
		given:;
			Statement input = 
				not(and(then(p), and(then(q), then(r))));
		
			Statement expected = 
				and(then(not(p)), and(then(not(q)), then(not(r))));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	
	@Test
	public void flatConjunction() throws Exception {
		given:;
			Statement input = 
				not(and(then(p), then(q), then(r)));
		
			Statement expected = 
				and(then(not(p)), then(not(q)), then(not(r)));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void recursiveConjunctionOfNegations() throws Exception {
		given:;
			Statement input = 
				not(and(not(then(p)), and(not(then(q)), not(then(r)))));
		
			Statement expected = 
				and(then(p), and(then(q), then(r)));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void flatConjunctionOfNegations() throws Exception {
		given:;
			Statement input = 
				not(and(not(then(p)), not(then(q)), not(then(r))));
		
			Statement expected = 
				and(then(p), then(q), then(r));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void negatedFlatConjunction() throws Exception {
		given:;
			Statement input = 
				not(not(and(then(p), then(q), then(r))));
		
			Statement expected = 
				and(then(p), then(q), then(r));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void flatConjunctionOfAlternateNegations() throws Exception {
		given:;
			Statement input = 
				not(and(then(p), not(then(q)), then(r), not(then(s)), then(t), not(then(u))));
		
			Statement expected = 
				and(then(not(p)), then(q), then(not(r)), then(s), then(not(t)), then(u));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	
	@Test
	public void whenTrivial() throws Exception {
		given:;
			Statement input = 
				not(when(p, then(q)));
		
			Statement expected = 
				when(p, then(not(q)));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void whenCompound() throws Exception {
		given:;
			Statement input = 
				not(when(p, and(then(q), then(r))));
		
			Statement expected = 
				when(p, and(then(not(q)), then(not(r))));

		when:;
			Statement result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

}
*/