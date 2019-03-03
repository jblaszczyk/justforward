package just.forward.normalization;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import just.forward.api.proposition.Literal;
import just.forward.api.proposition.Proposition;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class NormalizationPreconditionTest extends JustTest {

	@Test
	public void trivial() throws Exception {
		given:;
			Proposition input = 
				p;
		
			List<Literal> expected = ImmutableList.of( 
				p
			);

		when:;
			Normalization normalization = Normalization.newInstance();
			List<List<Literal>> result = normalization.precondition(input)
				.map(JustTest::toList)
				.collect(Collectors.toList());
			
		then:;
			assertThat( result ).isNotNull();
			assertThat( result ).contains( expected );
	}

}
