package just.forward.api.proposition;

import static just.forward.api.common.Sentences.*;
import static just.forward.api.common.Sentences.exists;
import static just.forward.api.common.Sentences.falseValue;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.or;
import static just.forward.api.common.Sentences.trueValue;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class PropositionSimplificationTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void negation() throws Exception {
		given:;
			Proposition input = 
				not(p);
		
			Proposition expected = 
				not(p);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void doubleNegation() throws Exception {
		given:;
			Proposition input = 
				not(not(p));
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	
	@Test
	public void tripleNegation() throws Exception {
		given:;
			Proposition input = 
				not(not(not(p)));
		
			Proposition expected = 
				not(p);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void recursiveConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, and(q, r));
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void recursiveDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, or(q, r));
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void flatConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q, r);
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void flatDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q, r);
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void recursiveConjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				and(not(not(not(p))), and(not(q), not(not(not(r)))));
		
			Proposition expected = 
				and(not(p), not(q), not(r));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void recursiveDisjunctionOfNegations() throws Exception {
		given:;
			Proposition input = 
				or(not(not(not(p))), or(not(q), not(not(not(r)))));
		
			Proposition expected = 
				or(not(p), not(q), not(r));

		when:;
			Proposition result = input.toSimplified();
		
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void negatedFlatConjunction() throws Exception {
		given:;
			Proposition input = 
				not(not(not(and(p, q, r))));
		
			Proposition expected = 
				or(not(p), not(q), not(r));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void negatedFlatDisjunction() throws Exception {
		given:;
			Proposition input = 
				not(not(not(or(p, q, r))));
		
			Proposition expected = 
				and(not(p), not(q), not(r));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void flatConjunctionOfAlternateNegations() throws Exception {
		given:;
			Proposition input = 
				and(p, not(not(not(q))), r, not(not(not(s))), t, not(not(not(u))));
		
			Proposition expected = 
				and(p, not(q), r, not(s), t, not(u));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void flatDisjunctionOfAlternateNegations() throws Exception {
		given:;
			Proposition input = 
				or(p, not(not(not(q))), r, not(not(not(s))), t, not(not(not(u))));
		
			Proposition expected = 
				or(p, not(q), r, not(s), t, not(u));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void cnf() throws Exception {
		given:;
			Proposition input = 
				and(or(or(p, q)), or(r, s), or(t, u));
		
			Proposition expected = 
				and(or(p, q), or(r, s), or(t, u));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void dnf() throws Exception {
		given:;
			Proposition input = 
				or(and(and(p, q)), and(r, s), and(t, u));
		
			Proposition expected = 
				or(and(p, q), and(r, s), and(t, u));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void unwrapOr() throws Exception {
		given:;
			Proposition input = 
				or(p);
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		
	
	@Test
	public void unwrapAnd() throws Exception {
		given:;
			Proposition input = 
				and(p);
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		
	
	
	
	
	
	
	
	
	
	
	@Test
	public void cnfOfNegations() throws Exception {
		given:;
			Proposition input = 
				and(or(not(not(not(p))), not(q)), or(not(not(not(r))), not(s)), or(not(not(not(t))), not(u)));
		
			Proposition expected = 
				and(or(not(p), not(q)), or(not(r), not(s)), or(not(t), not(u)));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void dnfOfNegations() throws Exception {
		given:;
			Proposition input = 
				or(and(not(not(not(p))), not(q)), and(not(not(not(r))), not(s)), and(not(not(not(t))), not(u)));
		
			Proposition expected = 
				or(and(not(p), not(q)), and(not(r), not(s)), and(not(t), not(u)));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void treeCentered() throws Exception {
		given:;
			Proposition input = 
				and(and(p, or(or(q, and(and(r, s)), t)), u));
		
			Proposition expected = 
				and(p, or(q, and(r, s), t), u);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void treeLeft() throws Exception {
		given:;
			Proposition input = 
				and(and( or(or( and(and( or(or( and(and(p, q)), r)), s)), t)), u));

			Proposition expected = 
				and(or(and(or(and(p, q), r), s), t), u);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void treeRight() throws Exception {
		given:;
			Proposition input = 
				and(and( p, or(or( q, and(and( r, or(or( s, and(and(t, u)) ))))))));
		
			Proposition expected = 
				and(p, or(q, and(r, or(s, and(t, u)))));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void treeCenteredWithNegationInside() throws Exception {
		given:;
			Proposition input = 
				and(p, not(not(not(or(q, and(r, s), t)))), u);
		
			Proposition expected = 
				and(p, not(q), or(not(r), not(s)), not(t), u);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void treeLeftWithNegationInside() throws Exception {
		given:;
			Proposition input = 
				and(or(not(not(not(and(or(and(p, q), r), s)))), t), u);
		
			Proposition expected = 
				and(or(and(or(not(p), not(q)), not(r)), not(s), t), u);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void treeRightWithNegationInside() throws Exception {
		given:;
			Proposition input = 
				and(p, or(q, not(not(not(and(r, or(s, and(t, u))))))));
		
			Proposition expected = 
				and(p, or(q, 
								not(r), and(not(s), or(not(t), not(u)))
						));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void selfComplementaryConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q, not(p));
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void selfComplementaryDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q, not(p));
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	@Ignore
	public void selfComplementaryConjunctionComplex() throws Exception {
		given:;
			Proposition input = 
				and(or(p, q), r, not(or(p, q)));
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Ignore
	@Test
	public void selfComplementaryDisjunctionComplex() throws Exception {
		given:;
			Proposition input = 
				or(or(p, q), r, not(or(p, q)));
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Ignore
	@Test
	public void selfComplementaryConjunctionComplex2() throws Exception {
		given:;
			Proposition input = 
				and(or(p, q), r, and(not(p), not(q)));
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Ignore
	@Test
	public void selfComplementaryDisjunctionComplex2() throws Exception {
		given:;
			Proposition input = 
				or(or(p, q), r, and(not(p), not(q)));
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void conjunctionWithTrueInside() throws Exception {
		given:;
			Proposition input = 
				and(p, q, trueValue(), r);
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void conjunctionWithFalseInside() throws Exception {
		given:;
			Proposition input = 
				and(p, q, falseValue(), r);
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
	
	@Test
	public void disjunctionWithTrueInside() throws Exception {
		given:;
			Proposition input = 
				or(p, q, trueValue(), r);
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}

	@Test
	public void disjunctionWithFalseInside() throws Exception {
		given:;
			Proposition input = 
				or(p, q, falseValue(), r);
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}
/*	
	@Test
	public void when1() throws Exception {
		given:;
			Proposition input = 
				when(p, q);
		
			Proposition expected = 
				when(p, q);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void when2() throws Exception {
		given:;
			Proposition input = 
//					when(and(p,p), and(Atom.trueValue(), q));
				when(p, and(q, r));
		
			Proposition expected = 
				when(p, and(q, r));

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void when3() throws Exception {
		given:;
			Proposition input = 
				when(p, and(trueValue(), q));
		
			Proposition expected = 
				when(p, q);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
	
	@Test
	public void whenTrivialTrue() throws Exception {
		given:;
			Proposition input = 
				when(trueValue(), q);
		
			Proposition expected = 
				q;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void whenAndTrue() throws Exception {
		given:;
			Proposition input = 
				and( p, when(trueValue(), q) );
		
			Proposition expected = 
				and( p, q);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void whenAndFalse() throws Exception {
		given:;
			Proposition input = 
				and( p, when(falseValue(), q) );
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toSimplified();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( cmp.compare(result, expected) ).isTrue();
	}	

	@Test
	public void whenTrivialFalse() throws Exception {
		given:;
			Proposition input = 
				when(falseValue(), q);
		
			Proposition expected = 
				empty();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	
*/


	@Test
	public void orAnd1() throws Exception {
		given:;
			Proposition input = 
				or(p, and( trueValue(), q));
		
			Proposition expected = 
				or(p, q);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void existsTrivial() throws Exception {
		given:;
			Proposition input = 
				exists(y, p);
		
			Proposition expected = 
				exists(y, p);

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}	

	@Test
	public void existsComplex() throws Exception {
		given:;
			Proposition input = 
				and( p, p, exists(y, and(q, q)) );
		
			Proposition expected = 
				and( p, exists(y, q) );

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		

	@Test
	public void andTrue() throws Exception {
		given:;
			Proposition input = 
				and( trueValue(), trueValue(), emptyProposition() );
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		

	@Test
	public void andFalse() throws Exception {
		given:;
			Proposition input = 
				and( trueValue(), falseValue(), emptyProposition() );
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		
	
	@Test
	public void orTrue() throws Exception {
		given:;
			Proposition input = 
				or( falseValue(), trueValue(), emptyProposition() );
		
			Proposition expected = 
				trueValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		

	@Test
	public void orFalse() throws Exception {
		given:;
			Proposition input = 
				and( falseValue(), falseValue(), emptyProposition() );
		
			Proposition expected = 
				falseValue();

		when:;
			Proposition result = input.toSimplified();
			
		then:;
//			assertThat( input.isSimplified() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
//			assertThat( result.isSimplified() ).isTrue();
	}		
	
}
