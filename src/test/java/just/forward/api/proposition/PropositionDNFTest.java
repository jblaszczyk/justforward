package just.forward.api.proposition;


import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Test;

import just.forward.test.JustTest;


// TODO
@SuppressWarnings("unused")
public class PropositionDNFTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Proposition expected = 
				p;

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void negation() throws Exception {
		given:;
			Proposition input = 
				not(p);
		
			Proposition expected = 
				not(p);

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void doubleNegation() throws Exception {
		given:;
			Proposition input = 
				not(not(p));
		
			Proposition expected = 
				not(not(p));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void recursiveConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, and(q, r));
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void recursiveDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, or(q, r));
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void flatConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q, r);
		
			Proposition expected = 
				and(p, q, r);

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void flatDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q, r);
		
			Proposition expected = 
				or(p, q, r);

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void cnf() throws Exception {
		given:;
			Proposition input = 
				and(or(p, q), or(r, s), or(t, u));
		
			Proposition expected =
				or(
					and(p, r, t),
					and(p, r, u),
					and(p, s, t),
					and(p, s, u),
					and(q, r, t),
					and(q, r, u),
					and(q, s, t),
					and(q, s, u)
				);

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void dnf() throws Exception {
		given:;
			Proposition input = 
				or(and(p, q), and(r, s), and(t, u));
		
			Proposition expected = 
				or(and(p, q), and(r, s), and(t, u));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void treeCentered() throws Exception {
		given:;
			Proposition input = 
				and(p, or(q, and(r, s), t), u);
//				and(p, u, or(q, and(r, s), t));
			
			Proposition expected = 
				or(and(p, u, q), and(p, u, r, s), and(p, u, t));
//				or( and(p, q), and(p, r, s), and(p, t), and(u, q), and(u, r, s), and(u, t) );

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	

	@Test
	public void treeLeft() throws Exception {
		given:;
			Proposition input = 
				and(or(and(or(and(p, q), r), s), t), u);

			Proposition expected =
				or(and(u, s, p, q), and(u, s, r), and(u, t));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	

	@Test
	public void treeRight() throws Exception {
		given:;
			Proposition input = 
				and(p, or(q, and(r, or(s, and(t, u)))));

			// or( and(p,q), and(p, r, or(s, and(t, u)))));
			// or( and(p,q), or(and(p,r,s), and(p,r,t, u)));
			
			Proposition expected = 
//				or( and(p, q), or(and(p, r, s), and(p, r, t, u)));
				or( and(p, q), and( p, r, s), and(p, r, t, u) );

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	
	@Test
	public void disjunctionWithConjuction() throws Exception {
		given:;
			Proposition input = 
				or(p, and(q, r));
		
			Proposition expected = 
				or(p, and(q, r));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void conjunctionWithDisjuction() throws Exception {
		given:;
			Proposition input = 
				and(p, or(q, r));
		
			Proposition expected = 
				or(and(p, q), and(p, r));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void disjunctionOfConjuctions2() throws Exception {
		given:;
			Proposition input = 
				or(and(p, q), and(r, s));
		
			Proposition expected = 
				or(and(p, q), and(r, s));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void conjunctionOfDisjuctions2() throws Exception {
		given:;
			Proposition input = 
				and(or(p, q), or(r, s));
		
			Proposition expected = 
				or( and(p, r), and(p, s), and(q, r), and(q, s) );

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}		
	
	@Test
	public void disjunctionWithConjuctionOfDisjunctions() throws Exception {
		given:;
			Proposition input = 
				or(p, and(or(q, r), or(s, t)));
		
			Proposition expected = 
				or(p, and(q, s), and(q, t), and(r, s), and(r, t));
//				or(p, or(and(q, s), and(q, t), and(r, s), and(r, t)));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void conjunctionWithDisjuctionOfConjunctions() throws Exception {
		given:;
			Proposition input = 
				and(p, or( and(q, r), and(s, t)));
		
			Proposition expected = 
				or( and(p, q, r), and(p, s, t) );

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}		

	@Test
	public void conjunctionWithAtomConjunctionDisjuction() throws Exception {
		given:;
			Proposition input = 
				and(p, and(s, t), or(q, r));
		
			Proposition expected = 
				or(and(p, s, t, q), and(p, s, t, r));

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}		

	@Test
	public void recursiveConjunctionWithExists() throws Exception {
		given:;
			Proposition input = 
				and(p, and(q, exists(y, r)));
		
			Proposition expected = 
				and(p, q, exists(y, r));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}	
	
	@Test
	public void cnfWithExists() throws Exception {
		given:;
			Proposition input = 
				and(or(p, q), or( exists(y, r), s), or(t, u));
		
			Proposition expected =
				or(
					and(p, exists(y, r), t),
					and(p, exists(y, r), u),
					and(p, s, t),
					and(p, s, u),
					and(q, exists(y, r), t),
					and(q, exists(y, r), u),
					and(q, s, t),
					and(q, s, u)
				);

		when:;
			Proposition result = input.toDNF();

		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testTrivial() throws Exception {
		given:;
			Proposition input = 
				p;
			Proposition expected = 
				p;
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testOr() throws Exception {
		given:;
			Proposition input = 
				or(p, q, r);
			Proposition expected = 
				or(p, q, r);
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testRecursiveOr() throws Exception {
		given:;
			Proposition input =
				or(p, or(q, r));
			Proposition expected =
				or(p, q, r);
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void testOrAnd() throws Exception {
		given:;
			Proposition input =
				or(p, and(q, r));
			Proposition expected =
				or(p, and(q, r));
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testAnd() throws Exception {
		given:;
			Proposition input =
				and(p,q,r);
			Proposition expected =
				and(p,q,r);
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testAndOr() throws Exception {
		given:;
			Proposition input =
				and(p, or(q, r));
			Proposition expected =
				or(and(p,q), and(p,r));
		
			
		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void testAndOr2() throws Exception {
		given:;
			Proposition input =
				and(
					or(p, q),
					or(r, s),
					or(t, u)
				);
			Proposition expected =
				or(
					and(p, r, t),
					and(p, r, u),
					and(p, s, t),
					and(p, s, u),
					and(q, r, t),
					and(q, r, u),
					and(q, s, t),
					and(q, s, u)
				);
		

		when:;
			Proposition result = input.toDNF();
		
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result.isDNF() ).isTrue();
			
			assertThat( result ).isEqualTo( expected );
			assertThat( result ).isInstanceOf(OrProposition.class);
			OrProposition orProposition = (OrProposition) result;
			assertThat( orProposition.getChildren() ).isNotNull();
			assertThat( orProposition.getChildren() ).isNotEmpty();
			assertThat( orProposition.getChildren() ).hasSize(8);
			
			assertThat( orProposition.getChildren() ).contains( and(p, r, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, r, u) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, u) );
			assertThat( orProposition.getChildren() ).contains( and(q, r, t) );
			assertThat( orProposition.getChildren() ).contains( and(q, r, u) );
			assertThat( orProposition.getChildren() ).contains( and(q, s, t) );
			assertThat( orProposition.getChildren() ).contains( and(q, s, u) );
	}

	@Test
	public void testAndOr3() throws Exception {
		given:;
			Proposition input =
				and(
					or(p, not(q)),
					or(r, s),
					or(t, u)
				);
			Proposition expected =
				or(
						and(p, r, t),
						and(p, r, u),
						and(p, s, t),
						and(p, s, u),
						and(not(q), r, t),
						and(not(q), r, u),
						and(not(q), s, t),
						and(not(q), s, u)

				);
		

		when:;
			Proposition result = input.toDNF();
		
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result.isDNF() ).isTrue();
			
			assertThat( result ).isEqualTo( expected );
			assertThat( result ).isInstanceOf(OrProposition.class);
			OrProposition orProposition = (OrProposition) result;
			assertThat( orProposition.getChildren() ).isNotNull();
			assertThat( orProposition.getChildren() ).isNotEmpty();
			assertThat( orProposition.getChildren() ).hasSize(8);
			
			assertThat( orProposition.getChildren() ).contains( and(p, r, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, r, u) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, u) );
			assertThat( orProposition.getChildren() ).contains( and(not(q), r, t) );
			assertThat( orProposition.getChildren() ).contains( and(not(q), r, u) );
			assertThat( orProposition.getChildren() ).contains( and(not(q), s, t) );
			assertThat( orProposition.getChildren() ).contains( and(not(q), s, u) );
	}

	@Test
	public void testAndOr4() throws Exception {
		given:;
			Proposition input =
				and(
					not(or(and(not(p), not(q)), and(not(r), not(s)))),
					or(t, u)
				);
			Proposition expected =
 				or(
						and(p, r, t),
						and(p, r, u),
						and(p, s, t),
						and(p, s, u),
						and(q, r, t),
						and(q, r, u),
						and(q, s, t),
						and(q, s, u)
				);

		when:;
			Proposition result = input.toNNF().toDNF();
		
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result.isDNF() ).isTrue();
			
			assertThat( result ).isEqualTo( expected );
			
			assertThat( result ).isInstanceOf(OrProposition.class);
			OrProposition orProposition = (OrProposition) result;
			assertThat( orProposition.getChildren() ).isNotNull();
			assertThat( orProposition.getChildren() ).isNotEmpty();
			assertThat( orProposition.getChildren() ).hasSize(8);
			
			assertThat( orProposition.getChildren() ).contains( and(p, r, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, r, u) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, t) );
			assertThat( orProposition.getChildren() ).contains( and(p, s, u) );
			assertThat( orProposition.getChildren() ).contains( and(q, r, t) );
			assertThat( orProposition.getChildren() ).contains( and(q, r, u) );
			assertThat( orProposition.getChildren() ).contains( and(q, s, t) );
			assertThat( orProposition.getChildren() ).contains( and(q, s, u) );
			
	}
	
	@Test
	public void forallTrivial() throws Exception {
		given:;
			Proposition input = 
				forall(x, p);
		
			Proposition expected = 
				forall(x, p);

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void existsTrivial() throws Exception {
		given:;
			Proposition input = 
				exists(x, p);
		
			Proposition expected = 
				exists(x, p);

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
	
	@Test
	public void forallDisjunction() throws Exception {
		given:;
			Proposition input = 
				forall(x, or(p, q));
		
			Proposition expected = 
				or(forall(x, p), forall(x, q));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void existsDisjunction() throws Exception {
		given:;
			Proposition input = 
				exists(x, or(p, q));
		
			Proposition expected = 
				or(exists(x, p), exists(x, q));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}

	@Test
	public void existsForAllDisjunction() throws Exception {
		given:;
			Proposition input = 
				exists(x, forall(y, or(p, q)));
		
			Proposition expected = 
				or(exists(x, forall(y, p)), exists(x, forall(y, q)));

		when:;
			Proposition result = input.toDNF();
			
		then:;
			assertThat( input.isDNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isDNF() ).isTrue();
	}
}
