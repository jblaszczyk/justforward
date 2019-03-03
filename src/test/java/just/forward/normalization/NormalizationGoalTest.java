package just.forward.normalization;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.atom;
import static just.forward.api.common.Sentences.or;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import just.forward.api.common.Predicate;
import just.forward.api.proposition.Literal;
import just.forward.api.proposition.Proposition;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class NormalizationGoalTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			Proposition expected = 
				p;

		when:;
			Normalization normalization = Normalization.newInstance();
			List<Literal> result = toList( normalization.goal(input) );
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).contains( expected );
	}

	@Test
	public void conjunction() throws Exception {
		given:;
			Proposition input = 
				and(p, q);
	
		when:;
			Normalization normalization = Normalization.newInstance();
			List<Literal> result = toList( normalization.goal(input) );
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).contains( p, q );
	}

	@Test
	public void disjunction() throws Exception {
		given:;
			Proposition input = 
				or(p, q);
	
		when:;
			Normalization normalization = Normalization.newInstance();
			List<Literal> result = toList( normalization.goal(input) );
			
		then:;
			assertThat( result ).isNotNull();
			
			assertThat( normalization.syntheticPredicates ).hasSize(1);
			Predicate synthPredicate = toList(normalization.syntheticPredicates).getFirst();
			assertThat( synthPredicate ).isNotNull();
			assertThat( result ).contains( atom(synthPredicate, x) );
			
			assertThat( normalization.syntheticRules ).hasSize(1);
			assertThat( normalization.syntheticRules.get(input) ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getPredicate() ).isEqualTo(synthPredicate);
			assertThat( normalization.syntheticRules.get(input).getBody() ).isNotNull();
			assertThat( normalization.syntheticRules.get(input).getBody() ).isEqualTo(input);
	}
	
}
