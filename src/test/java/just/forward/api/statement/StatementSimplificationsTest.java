package just.forward.api.statement;


import static org.fest.assertions.Assertions.assertThat;
import static just.forward.api.common.Sentences.*;

import org.junit.Ignore;
import org.junit.Test;

import just.forward.normalization.Normalization;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class StatementSimplificationsTest extends JustTest {

	@Test
	public void flattenAnd() throws Exception {
		given:;
			Statement input = 
				and(then(p), and(then(q), then(r)));
			Statement expected = 
				and(then(p), then(q), then(r));
			
		when:;
			Statement result = input.flatten();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void flattenComplex() throws Exception {
		given:;
			Statement input = 
				and(then(p), forall(x, and(and( when(p,when(q,then(q))), then(r)))));
			Statement expected = 
				and(then(p), forall(x, and( when(and(p,q),then(q)), then(r))));
			
		when:;
			Statement result = input.flatten();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pullAndOverForAll_forall_and() throws Exception {
		given:;
			Statement input = 
				forall(x, and(then(p),then(q)));
			Statement expected = 
				and(forall(x,then(p)), forall(x,then(q)));
			
		when:;
			Statement result = input.pullAndOverForAll();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pullAndOverForAll_forall_and_forall_and() throws Exception {
		given:;
			Statement input = 
				forall(x, 
					and(
						forall(y,
							and(
								then(py),
								then(qy)
							)
						),
						forall(z,
							and(
								then(pz),
								then(qz)
							)
						)
					)
				);
			Statement expected =
				and(
					forall( then(py), x, y),
					forall( then(qy), x, y),
					forall( then(pz), x, z),
					forall( then(qz), x, z)
				); 
			
		when:;
//			Statement result = input.pullAndOverForAll().pullAndOverForAll().flatten();
			Statement result = input.pullAndOverForAll();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void pullAndOverForAll_when_forall_and() throws Exception {
		given:;
			Statement input = 
				when(p, forall(x, and(then(p),then(q))));
			Statement expected = 
				when(p, and(forall(x,then(p)), forall(x,then(q))));
			
		when:;
			Statement result = input.pullAndOverForAll();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void pullAndOverWhen_when_and() throws Exception {
		given:;
			Statement input = 
				when(p, and(then(p),then(q)));
			Statement expected = 
				and(when(p,then(p)), when(p,then(q)));
			
		when:;
			Statement result = input.pullAndOverWhen();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pullAndOverWhen_when_and_when_and() throws Exception {
		given:;
			Statement input =
				when(px,
					and(
						when(qy,
							and( then(ry), then(sy))
						),
						when(qz, 
							and( then(rz), then(sz))
						)
					)
				);
			Statement expected = 
				and(
					when(and(px,qy), then(ry)), 
					when(and(px,qy), then(sy)),
					when(and(px,qz), then(rz)), 
					when(and(px,qz), then(sz))
				);
		when:;
//			Statement result = input.pullAndOverWhen().pullAndOverWhen().flatten();
			Statement result = input.pullAndOverWhen();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void pullAndOverWhen_forall_when_and() throws Exception {
		given:;
			Statement input = 
				forall(x, when(p, and(then(p),then(q))));
			Statement expected = 
				forall(x, and(when(p,then(p)), when(p,then(q))));
			
		when:;
			Statement result = input.pullAndOverWhen();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverForall_when_forall() throws Exception {
		given:;
			Statement input = 
				when(p, forall(x, then(q)));
			Statement expected = 
				forall(x, when(p,then(q)));
			
		when:;
			Statement result = input.pushWhenOverForall();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverForall_and_when_forall() throws Exception {
		given:;
			Statement input = 
				and(then(p), when(p, forall(x, then(q))));
			Statement expected = 
				and(then(p), forall(x, when(p,then(q))));
			
		when:;
			Statement result = input.pushWhenOverForall();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverOr() throws Exception {
		given:;
			Statement input = 
				when( or(p,q), then(p));
			Statement expected = 
				and( when(p,then(p)), when(q,then(p)) );
			
		when:;
			Statement result = input.pushWhenOverOr();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverOr_forall_when() throws Exception {
		given:;
			Statement input = 
				forall(x, when( or(p,q), then(p)));
			Statement expected = 
				forall(x, and( when(p,then(p)), when(q,then(p)) ));
			
		when:;
			Statement result = input.pushWhenOverOr();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	
	@Test
	public void pushWhenOverOrRecursive() throws Exception {
		given:;
			Statement input = 
				when( or(p,q),
					when( or(r,s),
						then(p)
					)
				);
			Statement expected = 
				and( 
					when(p, 
						and(
							when(r, then(p)),
							when(s, then(p))
						)
					),
					when(q, 
						and(
							when(r, then(p)),
							when(s, then(p))
						)
					)
				);
			
		when:;
			Statement result = input.pushWhenOverOr();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverOrRecursiveFlatten() throws Exception {
		given:;
			Statement input = 
				when( or(p,q),
					when( or(r,s),
						then(p)
					)
				);
			Statement expected = 
				and( 
					when(and(p,r), then(p)),
					when(and(p,s), then(p)),
					when(and(q,r), then(p)),
					when(and(q,s), then(p))
				);
			
		when:;
			Statement result = input.pushWhenOverOr().pullAndOverWhen().flatten();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void pushWhenOverForAll_WhenForallWhenForall() throws Exception {
		given:;
			Statement input =
				when(p, forall(x, when(q, forall(y, then(s)))));
			
			Statement expected =
				forall(when(and(p,q),then(s)), x, y);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = input.pushWhenOverForall().flatten();

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo(expected);
	}	

	@Test
	public void pushWhenOverForAll_ForallWhenForall() throws Exception {
		given:;
			Statement input =
				forall(x, when(q, forall(y, then(s))));
			
			Statement expected =
				forall(when(q,then(s)), x, y);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = input.pushWhenOverForall().flatten();

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo(expected);
	}	

	
	@Test
	public void cnf1() throws Exception {
		given:;
			Statement input = 
				forall(x, when(p, and(then(p),then(q))));
			Statement expected = 
				and(
					forall(x, when(p, then(p))),
					forall(x, when(p, then(q)))
				);
			
		when:;
			Statement result = input.toCNF();
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
}
