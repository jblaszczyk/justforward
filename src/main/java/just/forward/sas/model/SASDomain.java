package just.forward.sas.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import just.forward.api.common.QualifiedName;
import just.forward.api.expression.Constant;

@Deprecated
public class SASDomain {

	ImmutableSet<SASAction> actions;
	ImmutableSet<Constant> constants;
	
	QualifiedName name;
	
	SASDomain(QualifiedName name, Iterable<SASAction> actions, Iterable<Constant> constants) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(actions);
		Objects.requireNonNull(constants);
		this.name = name;
		this.actions = ImmutableSet.copyOf(actions);
		this.constants = ImmutableSet.copyOf(constants);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public QualifiedName getName() {
		return name;
	}
	
	public Set<Constant> getConstants() {
		return constants;
	}
	public Stream<Constant> constants() {
		return constants.stream();
	}
	
	public Set<SASAction> getActions() {
		return actions;
	}
	
	public Stream<SASAction> actions() {
		return actions.stream();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				actions,
				constants
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
		
		SASDomain other = (SASDomain) obj;
		return Objects.equals(this.actions, other.actions) &&
				Objects.equals(this.constants, other.constants);
	}

	public static class Builder {
		
		QualifiedName name;
		LinkedList<SASAction> actions = Lists.newLinkedList();
		LinkedList<Constant> constants = Lists.newLinkedList();
		
		Builder() {}
		
		public Builder name(QualifiedName name) {
			Objects.requireNonNull(name);
			this.name = name;
			return this;
		}
		
		public Builder name(String name) {
			this.name = QualifiedName.of(name);
			return this;
		}

		public Builder constant(Constant constant) {
			Objects.requireNonNull(constant);
			constants.add(constant);
			return this;
		}

		public Builder constant(String name) {
			return constant(Constant.newInstance(name));
		}
		
		public Builder constants(Iterable<Constant> constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, constants );
			return this;
		}
		
		public Builder constants(Stream<Constant> constants) {
			Objects.requireNonNull(constants);
			this.constants.addAll( constants.collect( Collectors.toList() ));
			return this;
		}

		public Builder constants(Constant... constants) {
			Objects.requireNonNull(constants);
			Iterables.addAll( this.constants, Arrays.asList(constants) );
			return this;
		}

		public Builder action(SASAction action) {
			Objects.requireNonNull(action);
			actions.add(action);
			return this;
		}
		
		public Builder actions(Iterable<SASAction> actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, actions );
			return this;
		}
		
		public Builder actions(Stream<SASAction> actions) {
			Objects.requireNonNull(actions);
			this.actions.addAll( actions.collect( Collectors.toList() ));
			return this;
		}
		
		public Builder actions(SASAction... actions) {
			Objects.requireNonNull(actions);
			Iterables.addAll( this.actions, Arrays.asList(actions) );
			return this;
		}		
		
		public SASDomain build() {
			if( actions.isEmpty() ) {
				throw new IllegalArgumentException("Domain should have at least one action");
			}

			return new SASDomain(name, actions, constants);
		}
	}
}
