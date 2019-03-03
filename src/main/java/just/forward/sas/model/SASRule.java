package just.forward.sas.model;

import java.util.Map;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import just.forward.api.common.QualifiedName;
import just.forward.api.proposition.Literal;

@Immutable
public class SASRule {

	SASPartialAssignmentImmutable body;
	SASVariableAssignmentImmutable assignment;
	
	QualifiedName name;
	
	SASRule(QualifiedName name, AbstractSASPartialAssignment body, SASVariable affectedVariable, Literal derivedValue) {
		Objects.requireNonNull(body);
		Objects.requireNonNull(affectedVariable);
		Objects.requireNonNull(derivedValue);
		Objects.requireNonNull(name);
		this.name = name;
		this.body = SASPartialAssignmentImmutable.copyOf(body);
		this.assignment = SASVariableAssignmentImmutable.of(affectedVariable, derivedValue);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public SASPartialAssignmentImmutable getBody() {
		return body;
	}

	public SASVariable getAffectedVariable() {
		return assignment.getVariable();
	}
	
	public Literal getDerivedValue() {
		return assignment.getValue();
	}
	
	public Map.Entry<SASVariable, Literal> getHead() {
		return assignment;
	}
	
	public QualifiedName getName() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				body,
				assignment
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		SASRule other = (SASRule) obj;
		return 	Objects.equals(this.body, other.body) &&
				Objects.equals(this.assignment, other.assignment);
	}
	
	@Override
	public String toString() {
		return name.toString() + ": (" + getAffectedVariable().toString() + "=" + getDerivedValue().toString() + " :- " + body.toString();
	}

	public static class Builder {
		
		SASPartialAssignmentMutable body;
		SASVariable affectedVariable;
		Literal derivedValue;
		QualifiedName name;

		Builder() {
		}
		
		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}
		
		public Builder body(AbstractSASPartialAssignment partialAssignment) {
			Objects.requireNonNull(partialAssignment);
			if( this.body == null ) {
				this.body = SASPartialAssignmentMutable.copyOf(partialAssignment);
			} else {
				this.body.merge(partialAssignment);
			}
			
			return this;
		}
		
		public Builder affectedVariable(SASVariable affectedVariable) {
			Objects.requireNonNull(affectedVariable);
			this.affectedVariable = affectedVariable;
			return this;
		}

		public Builder derivedValue(Literal derivedValue) {
			Objects.requireNonNull(derivedValue);
			this.derivedValue = derivedValue;
			return this;
		}
		
		public SASRule build() {
			return new SASRule(name, body, affectedVariable, derivedValue);
		}
	}
	
}
