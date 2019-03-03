package just.forward.api.statement;


import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Test;

import just.forward.test.JustTest;

// TODO
@SuppressWarnings("unused")
public class StatementPNFTest extends JustTest {

	@Test
	public void testTrivial() throws Exception {
		given:;
			Statement input = 
				then(p);
			Statement expected = 
				then(p);
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}

	@Test
	public void testTrivialForall() throws Exception {
		given:;
			Statement input = 
				forall(y, then(p));
			Statement expected = 
				forall(y, then(p));
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isTrue();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}
	
	@Test
	public void testForall1() throws Exception {
		given:;
			Statement input = 
				and(then(p), forall(y, then(q)));
			Statement expected = 
				forall(y, and(then(p), then(q)));
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}

	@Test
	public void testForallNested() throws Exception {
		given:;
			Statement input = 
				and(then(p), forall(y, forall(z, forall(w, then(q)))));
			Statement expected = 
				forall(and(then(p), then(q)), y, z, w);
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}		

	@Test
	public void whenTrivialForall() throws Exception {
		given:;
			Statement input = 
				when(p, forall(y, then(q)));
			Statement expected = 
				forall(y, when(p, then(q)));
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}
	
/*	@Test
	public void negation() throws Exception {
		given:;
			Statement input = 
				not(forall(y, then(p)));
			Statement expected = 
				forall(y, then(not(p)));
			
		when:;
			Statement result = input.toPNF();
			
		then:;
			assertThat( input.isPNF() ).isFalse();
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
			assertThat( result.isPNF() ).isTrue();
	}
*/
}
