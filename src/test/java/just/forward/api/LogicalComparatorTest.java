package just.forward.api;

import static just.forward.api.common.Sentences.and;
import static just.forward.api.common.Sentences.falseValue;
import static just.forward.api.common.Sentences.not;
import static just.forward.api.common.Sentences.or;
import static just.forward.api.common.Sentences.trueValue;
import static just.forward.api.common.Sentences.var;
import static just.forward.api.common.Sentences.when;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import just.forward.api.expression.Variable;
import just.forward.api.proposition.Proposition;
import just.forward.test.JustTest;

@SuppressWarnings("unused")
public class LogicalComparatorTest extends JustTest {

	@Test
	public void testTrivial() throws Exception {
		given:;
			Proposition arg1 =
				p;
			
			Proposition arg2 =
				p;
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}

	@Test
	public void testNoMatch() throws Exception {
		given:;
			Proposition arg1 =
				p;
			
			Proposition arg2 =
				q;
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isFalse();
	}
	
	@Test
	public void testAnd() throws Exception {
		given:;
			Proposition arg1 =
				and(p, q);
			
			Proposition arg2 =
				and(q, p);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}

	@Test
	public void testAndTooManyAtoms1() throws Exception {
		given:;
			Proposition arg1 =
				and(p, q, r);
			
			Proposition arg2 =
				and(p, q);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isFalse();
	}
	
	@Test
	public void testAndTooManyAtoms2() throws Exception {
		given:;
			Proposition arg1 =
				and(p, q);
			
			Proposition arg2 =
				and(p, q, r);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isFalse();
	}
	
	@Test
	public void testAndIdentity1() throws Exception {
		given:;
			Proposition arg1 =
				and(p, p);
			
			Proposition arg2 =
				p;
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}
	
	@Test
	public void testOr() throws Exception {
		given:;
			Proposition arg1 =
				or(p, q);
			
			Proposition arg2 =
				or(q, p);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}

	@Test
	public void testOrTooManyAtoms1() throws Exception {
		given:;
			Proposition arg1 =
				or(p, q, r);
			
			Proposition arg2 =
				or(p, q);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isFalse();
	}
	
	@Test
	public void testOrTooManyAtoms2() throws Exception {
		given:;
			Proposition arg1 =
				or(p, q);
			
			Proposition arg2 =
				or(p, q, r);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isFalse();
	}
	
	@Test
	public void testOrIdentity1() throws Exception {
		given:;
			Proposition arg1 =
				or(p, p);
			
			Proposition arg2 =
				p;
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}	
	
	@Test
	public void testAndComplementary() throws Exception {
		given:;
			Proposition arg1 =
				and(p, not(p));
			
			Proposition arg2 =
				falseValue();
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}

	@Test
	public void testOrComplementary() throws Exception {
		given:;
			Proposition arg1 =
				or(p, not(p));
			
			Proposition arg2 =
				trueValue();
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}

/*
	@Test
	public void whenTrue() throws Exception {
		given:;
			Proposition arg1 =
				or(p, when(trueValue(), q));
			
			Proposition arg2 =
				or(p, q);
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}
	@Test
	public void whenFalse() throws Exception {
		given:;
			Proposition arg1 =
				or(p, when(falseValue(), q));
			
			Proposition arg2 =
				p;
			
		when:;
			LogicalComparator cmp = LogicalComparator.newInstance();
			
		then:;
			assertThat( cmp.compare(arg1, arg2) ).isTrue();
	}
*/
	// TODO forall, exists
}
