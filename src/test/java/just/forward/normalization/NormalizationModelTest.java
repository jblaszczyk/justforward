package just.forward.normalization;

import static just.forward.api.common.Sentences.then;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import just.forward.api.model.Operator;
import just.forward.normalization.model.NormalizedEffect;
import just.forward.normalization.model.NormalizedOperator;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class NormalizationModelTest extends JustTest {

	@Test
	public void operatorTrivial() throws Exception {
		given:;
			Operator input = Operator.builder()
				.name("name")
				.parameter(x)
				.precondition( p )
				.effect( then(q) )
			.build();
			
			NormalizedOperator expected = NormalizedOperator.builder()
				.name("name")
				.parameter(x)
				.precondition( p )
				.effect( NormalizedEffect.builder()
					.effect( q )
					.build()
				)
			.build();

		when:;
			Normalization normalization = Normalization.newInstance();
			List<NormalizedOperator> result = toList( normalization.operator(input) );
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).contains( expected );
	}

}
