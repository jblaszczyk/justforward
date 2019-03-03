package just.forward.api.proposition;

import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Test;

import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class PropositionNNFTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	@Test
	public void negation() throws Exception {
		given:;
			Proposition input = 
				not(p);
		
			Proposition expected = 
				p.toNegated();

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void doubleNegation() throws Exception {
		given:;
			Proposition input = 
				not(not(p));
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void tripleNegation() throws Exception {
		given:;
			Proposition input = 
				not(not(not(p)));
		
			Proposition expected = 
				not(p);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isTrue(); // note that not() returns AtomLiteral
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void recursiveConjunction() throws Exception {
		given:;
			Proposition input = 
				not(and(p, and(q, r)));
		
			Proposition expected = 
				or(not(p), or(not(q), not(r)));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void recursiveDisjunction() throws Exception {
		given:;
			Proposition input = 
				not(or(p, or(q, r)));
		
			Proposition expected = 
				and(not(p), and(not(q), not(r)));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void flatConjunction() throws Exception {
		given:;
			Proposition input = 
				not(and(p, q, r));
		
			Proposition expected = 
				or(not(p), not(q), not(r));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void flatDisjunction() throws Exception {
		given:;
			Proposition input = 
				not(or(p, q, r));
		
			Proposition expected = 
				and(not(p), not(q), not(r));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void recursiveConjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				not(and(not(p), and(not(q), not(r))));
		
			Proposition expected = 
				or(p, or(q, r));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void recursiveDisjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				not(or(not(p), or(not(q), not(r))));
		
			Proposition expected = 
				and(p, and(q, r));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	
	
	@Test
	public void flatConjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				not(and(not(p), not(q), not(r)));
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void flatDisjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				not(or(not(p), not(q), not(r)));
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void negatedFlatConjunction() throws Exception {
		given:;
			Proposition input = 
				not(not(and(p, q, r)));
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void negatedFlatDisjunction() throws Exception {
		given:;
			Proposition input = 
				not(not(or(p, q, r)));
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void flatConjunctionOfAlternateNegations() throws Exception {
		given:;
			Proposition input = 
				not(and(p, not(q), r, not(s), t, not(u)));
		
			Proposition expected = 
				or(not(p), q, not(r), s, not(t), u);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void flatDisjunctionOfAlternateNegations() throws Exception {
		given:;
			Proposition input = 
				not(or(p, not(q), r, not(s), t, not(u)));
		
			Proposition expected = 
				and(not(p), q, not(r), s, not(t), u);

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	
	
	@Test
	public void cnf() throws Exception {
		given:;
			Proposition input = 
				not(and(or(p, q), or(r, s), or(t, u)));
		
			Proposition expected = 
				or(and(not(p), not(q)), and(not(r), not(s)), and(not(t), not(u)));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void dnf() throws Exception {
		given:;
			Proposition input = 
				not(or(and(p, q), and(r, s), and(t, u)));
		
			Proposition expected = 
				and(or(not(p), not(q)), or(not(r), not(s)), or(not(t), not(u)));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void cnfOfNegations() throws Exception {
		given:;
			Proposition input = 
				not(and(or(not(p), not(q)), or(not(r), not(s)), or(not(t), not(u))));
		
			Proposition expected = 
				or(and(p, q), and(r, s), and(t, u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void dnfOfNegations() throws Exception {
		given:;

			Proposition input = 
				not(or(and(not(p), not(q)), and(not(r), not(s)), and(not(t), not(u))));
		
			Proposition expected = 
				and(or(p, q), or(r, s), or(t, u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}

	@Test
	public void treeCentered() throws Exception {
		given:;
	
			Proposition input = 
				not(and(p, or(q, and(r, s), t), u));
		
			Proposition expected = 
				or(not(p), and(not(q), or(not(r), not(s)), not(t)), not(u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	

	@Test
	public void treeLeft() throws Exception {
		given:;

			Proposition input = 
				not(and(or(and(or(and(p, q), r), s), t), u));
		
			Proposition expected = 
				or(and(or(and(or(not(p), not(q)), not(r)), not(s)), not(t)), not(u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	

	@Test
	public void treeRight() throws Exception {
		given:;

			Proposition input = 
				not(and(p, or(q, and(r, or(s, and(t, u))))));
		
			Proposition expected = 
				or(not(p), and(not(q), or(not(r), and(not(s), or(not(t), not(u))))));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}
	
	@Test
	public void treeCenteredWithNegationInside() throws Exception {
		given:;

			Proposition input = 
				not(and(p, not(or(q, and(r, s), t)), u));
		
			Proposition expected = 
				or(not(p), or(q, and(r, s), t), not(u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	

	@Test
	public void treeLeftWithNegationInside() throws Exception {
		given:;

			Proposition input = 
				not(and(or(not(and(or(and(p, q), r), s)), t), u));
		
			Proposition expected = 
				or(and(and(or(and(p, q), r), s), not(t)), not(u));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	

	@Test
	public void treeRightWithNegationInside() throws Exception {
		given:;

			Proposition input = 
				not(and(p, or(q, not(and(r, or(s, and(t, u)))))));
		
			Proposition expected = 
				or(not(p), and(not(q), and(r, or(s, and(t, u)))));

		when:;
			Proposition result = input.toNNF();
			
		then:;
			assertThat( input.isNNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isNNF() ).isTrue();
	}	

}
