package just.forward.api.proposition;


import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Test;

import just.forward.test.JustTest;

// TODO
@SuppressWarnings("unused")
public class PropositionPNFTest extends JustTest {

	@Test
	public void testTrivial() throws Exception {
		given:;
			Proposition input = 
				p;
			Proposition expected = 
				p;
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}

	@Test
	public void testTrivialExists() throws Exception {
		given:;
			Proposition input = 
				exists(y, p);
			Proposition expected = 
				exists(y, p);
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}

	@Test
	public void testTrivialForall() throws Exception {
		given:;
			Proposition input = 
				forall(y, p);
			Proposition expected = 
				forall(y, p);
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}
	
	@Test
	public void testExists1() throws Exception {
		given:;
			Proposition input = 
				and(p, exists(y, q));
			Proposition expected = 
				exists(y, and(p, q));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}
	
	@Test
	public void testForall1() throws Exception {
		given:;
			Proposition input = 
				and(p, forall(y, q));
			Proposition expected = 
				forall(y, and(p, q));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}

	@Test
	public void testForallNested() throws Exception {
		given:;
			Proposition input = 
				and(p, forall(y, forall(z, forall(w, q))));
			Proposition expected = 
					forall(and(p, q), y, z, w);
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		

	@Test
	public void testNestedSameOrdering1() throws Exception {
		given:;
			Proposition input = 
				and(p, exists(y, forall(z, exists(w, q))));
			Proposition expected = 
				exists(y, forall(z, exists(w, and(p, q))));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		

	@Test
	public void testNestedSameOrdering2() throws Exception {
		given:;
			Proposition input = 
				and(p, forall(y, exists(z, forall(w, q))));
			Proposition expected = 
				forall(y, exists(z, forall(w, and(p, q))));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		
	
	@Test
	public void testNested1() throws Exception {
		given:;
			Proposition input = 
				and(p, exists(y, forall(z, forall(w, q))));
			Proposition expected = 
				exists(y, forall(and(p, q), z, w));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		

	@Test
	public void testNested2() throws Exception {
		given:;
			Proposition input = 
				and(p, exists(y, forall(z, forall(w, q))));
			Proposition expected = 
				exists(y, forall(and(p, q), z, w));
			
		when:;
			Proposition result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		
}
