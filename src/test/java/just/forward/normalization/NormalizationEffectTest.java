package just.forward.normalization;

import static just.forward.api.common.Sentences.then;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import just.forward.api.statement.Statement;
import just.forward.normalization.model.NormalizedEffect;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class NormalizationEffectTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Statement input = 
				then(p);
		
			NormalizedEffect expected =  NormalizedEffect.builder()
				.effect(p)
			.build();

		when:;
			Normalization normalization = Normalization.newInstance();
			List<NormalizedEffect> result = toList( normalization.effect(input) );
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).contains( expected );
	}

}
