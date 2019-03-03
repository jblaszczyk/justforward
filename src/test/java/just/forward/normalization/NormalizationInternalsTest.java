package just.forward.normalization;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.exists;
import static just.forward.api.common.Sentences.forall;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.or;
import static just.forward.api.common.Sentences.then;
import static just.forward.api.common.Sentences.when;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import just.forward.api.common.Predicate;
import just.forward.api.proposition.AndProposition;
import just.forward.api.proposition.AtomLiteral;
import just.forward.api.proposition.Proposition;
import just.forward.api.statement.Statement;
import just.forward.normalization.Normalization;
import just.forward.normalization.model.NormalizedEffect;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class NormalizationInternalsTest extends JustTest {

	@Test
	public void collectForAll1() throws Exception {
		given:;
			Proposition input = 
				and(forall(x,px), or(px, forall(y,py), not(forall(z,pz))));
		
		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.collectForAll(input).collect(Collectors.toSet());
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).hasSize(3);
			assertThat( result ).contains(forall(x,px));
			assertThat( result ).contains(forall(y,py));
			assertThat( result ).contains(forall(z,pz));
	}

	@Test
	public void goalTrivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Proposition expected = 
				p;

		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void goalConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q);
		
			Proposition expected = 
				and(p, q);

		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void goalDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q);
		
		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isInstanceOf(AtomLiteral.class);
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( ((AtomLiteral) result).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(input) ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(input).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getBody() ).isEqualTo(input);
	}
	
	@Test
	public void goalExists() throws Exception {
		given:;
			Proposition input = 
				exists(x, px);
		
		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isInstanceOf(AtomLiteral.class);
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( ((AtomLiteral) result).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(input) ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(input).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getBody() ).isEqualTo(input);
	}

	@Test
	public void goalExistsComplex() throws Exception {
		given:;
			Proposition input = 
				and(px, exists(y, qy));
		
		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isInstanceOf(AtomLiteral.class);
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( ((AtomLiteral) result).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(input) ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(input).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getBody() ).isEqualTo(input);
	}
	
	@Test
	public void goalForAll() throws Exception {
		given:;
			Proposition input = 
				forall(x, px);

			Proposition expected = 
				exists(x, not(px));
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isInstanceOf(AtomLiteral.class);
			assertThat( ((AtomLiteral) result).isNegated() ).isTrue();
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( ((AtomLiteral) result).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(expected) ).isNotNull();
			assertThat( normalization.syntheticRules.get(expected).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(expected).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(expected).getBody() ).isEqualTo(expected);
	}	

	@Test
	public void goalForAllComplex() throws Exception {
		given:;
			Proposition input = 
				and(px, forall(y, qy), forall(z, pz));

		when:;
			Normalization normalization = Normalization.newInstance();
			Proposition result = normalization.normalizeGoal(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isInstanceOf(AndProposition.class);
			assertThat( normalization.syntheticPredicates ).hasSize(2);
			assertThat( normalization.syntheticRules ).hasSize(2);
			assertThat( normalization.syntheticRules.containsKey(exists(y, not(qy))) ).isTrue();
			assertThat( normalization.syntheticRules.containsKey(exists(z, not(pz))) ).isTrue();
	}	
	
	
	@Test
	public void preconditionTrivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Set<Proposition> expected = ImmutableSet.of(
				p
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void preconditionConjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q);
		
			Set<Proposition> expected = ImmutableSet.of(
				and(p, q)
			);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void preconditionExists() throws Exception {
		given:;
			Proposition input = 
				exists(x, px);
		
			Set<Proposition> expected = ImmutableSet.of(
				exists(x, px)
			);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void preconditionDisjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q);
		
			Set<Proposition> expected =  ImmutableSet.of(
				p, q
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void preconditionToDNF() throws Exception {
		given:;
			Proposition input = 
				and(p, or(q, r), s);
		
			Set<Proposition> expected =  ImmutableSet.of(
				and(p, q, s),
				and(p, r, s)
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void preconditionExistsDisjunction() throws Exception {
		given:;
			Proposition input = 
				exists(x, or(px, qx));
		
			Set<Proposition> expected = ImmutableSet.of(
				exists(x, px), exists(x, qx)
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void preconditionDisjunctionExists() throws Exception {
		given:;
			Proposition input = 
				or(exists(x, px), exists(x, qx));
		
			Set<Proposition> expected = ImmutableSet.of(
				exists(x, px), 
				exists(x, qx)
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void preconditionForAll() throws Exception {
		given:;
			Proposition input = 
				forall(x, px);

			Proposition expected = 
				exists(x, not(px));
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).hasSize(1);
			Proposition innerResult = toList(result).getFirst();
			assertThat( innerResult ).isInstanceOf(AtomLiteral.class);
			assertThat( ((AtomLiteral) innerResult).isNegated() ).isTrue();
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( ((AtomLiteral) innerResult).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(expected) ).isNotNull();
			assertThat( normalization.syntheticRules.get(expected).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(expected).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(expected).getBody() ).isEqualTo(expected);
	}	
	
	@Test
	public void preconditionSimplify() throws Exception {
		given:;
			Proposition input = 
				and(p, and(q, and( r)));
		
			Set<Proposition> expected =  ImmutableSet.of(
				and(p, q, r)
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void preconditionToPNF() throws Exception {
		given:;
			Proposition input = 
				and(p, exists(x, exists(y, and(rx, ry))));
		
			Set<Proposition> expected =  ImmutableSet.of(
				exists( and(p, rx, ry), x,y)
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			Set<Proposition> result = normalization.normalizePrecondition(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void effectTrivial() throws Exception {
		given:;
			Statement input = 
				then(p);
		
			Statement expected = 
				then(p);

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void effectConjunction() throws Exception {
		given:;
			Statement input = 
				and(then(p), then(q));
	
			Statement expected = 
				and(then(p), then(q));

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}	
	
	@Test
	public void effectForAll() throws Exception {
		given:;
			Statement input = 
				forall(x, then(px));
		
			Statement expected = 
				forall(x, then(px));

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void effectDistributeForAllOverConjunctions() throws Exception {
		given:;
			Statement input = 
				forall(x, and(then(px), then(qx)));
		
			Statement expected = 
				and( forall(x, then(px)), forall(x, then(qx)) );

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void effectDistributeWhenConditionDisjunction() throws Exception {
		given:;
			Statement input = 
				when( or(p, q), then(r) );
		
			Statement expected = 
				and( 
					when(p, then(r)), 
					when(q, then(r))
				);

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void effectDistributeWhenEffectConjuntion() throws Exception {
		given:;
			Statement input = 
				when( r, and(then(p), then(q)) );
		
			Statement expected = 
				and( 
					when(r, then(p)), 
					when(r, then(q))
				);

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}	
	
	@Test
	public void effectPullForAllOverWhen() throws Exception {
		given:;
			Statement input = 
				when(p, forall(x, then(qx)));
		
			Statement expected = 
				forall(x, when(p, then(qx)));

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}	
	
	@Test
	public void effectFlattenWhen() throws Exception {
		given:;
			Statement input = 
				when(p, when(q, then(r)));
		
			Statement expected = 
				when( and(p, q), then(r));

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}	

	@Test
	public void effectFlattenConjuntions() throws Exception {
		given:;
			Statement input = 
				and(then(p), and(then(q), then(r)));
		
			Statement expected = 
				and(then(p), then(q), then(r));

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}	
	
	@Test
	public void effectPullForAll2() throws Exception {
		given:;
			Statement input = 
				and( 
					then(p), 
					when(p, 
						forall(x, forall(y, 
							then(px)
						))
					) 
				);
		
			Statement expected = 
				and( 
					then(p), 
					forall( 
						when(p, 
							then(px)
						),
					x,y)
				);

		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	// precondition: (p1 and forall(x1, p2) or (p3 and forall(x2, p4 and forall(x3, p5)
	// ==>   (p1 and q1) or (p3 and q2)

	
	// goal: (p and (q or s)) => q1
	// goal: (p and exists(x, q)) => q1
	
	// eff: when(p or q, e) ==> when(p, e) and when(q, e)
	
	@Test
	public void convertLiteral() throws Exception {
		given:;
			Statement input =
				then(p);
			
			NormalizedEffect expected = NormalizedEffect.builder()
				.effect(p)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			NormalizedEffect result = normalization.convertOneStatement(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void convertWhenStatement() throws Exception {
		given:;
			Statement input =
				when(p, then(q));
			
			NormalizedEffect expected = NormalizedEffect.builder()
				.condition(p)
				.effect(q)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			NormalizedEffect result = normalization.convertOneStatement(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void convertForAll() throws Exception {
		given:;
			Statement input =
				forall(x, then(p));
			
			NormalizedEffect expected = NormalizedEffect.builder()
				.variable(x)
				.effect(p)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			NormalizedEffect result = normalization.convertOneStatement(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}
	
	@Test
	public void convertForAllWhenStatement() throws Exception {
		given:;
			Statement input =
				forall(x, when(p, then(q)));
			
			NormalizedEffect expected = NormalizedEffect.builder()
				.variable(x)
				.condition(p)
				.effect(q)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			NormalizedEffect result = normalization.convertOneStatement(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo( expected );
	}

	@Test
	public void convertAndStatement() throws Exception {
		given:;
			Statement input =
				and(
					then(p),
					forall(x, when(p, then(q)))
				);
			
			NormalizedEffect expected1 = NormalizedEffect.builder()
					.effect(p)
				.build();
			
			NormalizedEffect expected2 = NormalizedEffect.builder()
				.variable(x)
				.condition(p)
				.effect(q)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			List<NormalizedEffect> result = toList( normalization.convertStatement(input) );

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).hasSize(2);
			assertThat( result ).contains(expected1);
			assertThat( result ).contains(expected2);
	}
	
	@Test
	public void effectForallWhenForall() throws Exception {
		given:;
			Statement input =
				forall(x, when(q, forall(y, then(s))));
			
			Statement expected =
				forall(when(q,then(s)), x, y);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo(expected);
	}	

	@Test
	public void effectWhenForallWhenForall() throws Exception {
		given:;
			Statement input =
				when(p, forall(x, when(q, forall(y, then(s)))));
			
			Statement expected =
				forall(when(and(p,q),then(s)), x, y);
			
		when:;
			Normalization normalization = Normalization.newInstance();
			Statement result = normalization.normalizeEffect(input);

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).isEqualTo(expected);
	}	
	
	@Test
	public void normalizeEffect() throws Exception {
		given:;
			Statement input =
				when(p, forall(x, when(q, forall(y, then(s)))));
			
			NormalizedEffect expected = NormalizedEffect.builder()
				.variables(x, y)
				.conditions(p, q)
				.effect(s)
			.build();
			
		when:;
			Normalization normalization = Normalization.newInstance();
			List<NormalizedEffect> result = toList( normalization.effect(input) );

		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).hasSize(1);
			assertThat( result ).contains(expected);
	}	
	
}
