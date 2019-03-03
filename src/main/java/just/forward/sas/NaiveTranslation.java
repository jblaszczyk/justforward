package just.forward.sas;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Maps;

import just.forward.api.proposition.Literal;
import just.forward.normalization.model.GroundNormalizedAction;
import just.forward.normalization.model.GroundNormalizedTask;
import just.forward.normalization.model.NormalizedEffect;
import just.forward.sas.model.AbstractSASPartialAssignment;
import just.forward.sas.model.SASAction;
import just.forward.sas.model.SASConditionalAssignment;
import just.forward.sas.model.SASPartialAssignmentMutable;
import just.forward.sas.model.SASTask;
import just.forward.sas.model.SASVariable;
import just.forward.sas.model.SASVariableDomain;

public class NaiveTranslation {

	Map<Literal, SASVariable> literal2variable = Maps.newHashMap();
	
	public SASTask translate(GroundNormalizedTask task) {
		SASTask.Builder builder = SASTask.builder();
		
		// TODO
		builder.name(task.getName());
		builder.original(task);
		builder.init( translateLiterals( task.getInit().stream() ) );
		builder.goal( translateLiterals( task.getGoal().stream() ) );
		builder.objects( task.objects() );
		builder.actions(
			task.actions()
				.map(this::translateAction)
		);
		
		return builder.build();
	}

	AbstractSASPartialAssignment translateLiterals(Stream<Literal> literals) {
		SASPartialAssignmentMutable result = SASPartialAssignmentMutable.newInstance();
		literals
			.forEach(e -> result.unify(var(e), e));
		
		return result;
	}
	
	SASAction translateAction(GroundNormalizedAction action) {
		return SASAction.builder()
			.name(action.getName())
			.preconditions( translateLiterals(action.preconditions() ) )
			.effect( translateEffects( action.effects() ))
		.build();
	}
	
	SASConditionalAssignment translateEffects(Stream<NormalizedEffect> effects) {
		return null; // TODO
	}
	
	SASVariable var(Literal literal) {
		Objects.requireNonNull(literal);
		Literal key = literal.isNegated() ? literal.toNegated() : literal;
		
		if( !literal2variable.containsKey(key) ) {
			SASVariableDomain varDomain = SASVariableDomain.builder()
				.atom(key)
				.atom(key.toNegated())
			.build();
			
			SASVariable sasVariable = SASVariable.newInstance(varDomain);
			literal2variable.put( key, sasVariable );
		}
		
		return literal2variable.get(key);
	}
}
