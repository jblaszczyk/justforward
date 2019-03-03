package just.forward.sas.model;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import just.forward.api.common.QualifiedName;

@Immutable
public class SASVariable {

	@Deprecated
	@Nullable
	QualifiedName name; // TODO needed?
	SASVariableDomain domain;
	
	SASVariable(@Nullable QualifiedName name, SASVariableDomain domain) {
//		Objects.requireNonNull(name);
		Objects.requireNonNull(domain);
		this.name = name;
	}
	
	public static SASVariable newInstance(SASVariableDomain domain) {
		return new SASVariable(null, domain);
	}

	public static SASVariable newInstance(QualifiedName name, SASVariableDomain domain) {
		return new SASVariable(name, domain);
	}
	
	public static SASVariable newInstance(String name, SASVariableDomain domain) {
		return new SASVariable(QualifiedName.of(name), domain);
	}
	
	public SASVariableDomain getDomain() {
		return domain;
	}
	
	public QualifiedName getName() {
		return name;
	}

	@Override
	public String toString() {
		return "$" + name.toString();
	}
	
	
}
