package just.forward.api.common;

import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class QualifiedName {

	private static final String NONAME = "anonymous";
	private static final QualifiedName DEFAULT = of(NONAME);
	
	private final Namespace namespace;
	private final String simpleName;
	
	private QualifiedName(Namespace namespace, String simpleName) {
		Objects.requireNonNull(namespace);
		this.namespace = namespace;
		this.simpleName = (simpleName == null || simpleName.isEmpty()) ? NONAME : simpleName;
	}
	
	public static QualifiedName of(Namespace namespace, String simpleName) {
		return new QualifiedName(namespace, simpleName);
	}
	
	public static QualifiedName of(String simpleName) {
		return new QualifiedName(Namespace.getDefault(), simpleName);
	}
	
	public static QualifiedName getDefault() {
		return DEFAULT;
	}
	
	public Namespace toNamespace() {
		return Namespace.of( Stream.concat( namespace.segments(), Stream.of(getSimpleName()) ) );
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public String getSimpleName() {
		return simpleName;
	}
	
	public String getQualifiedName() {
		return getNamespace().toString() + Namespace.DELIMITER + getSimpleName();
	}

	@Override
	public String toString() {
		if( Namespace.getDefault().equals(getNamespace()) ) {
			return getSimpleName();
		}
		
		return getQualifiedName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		QualifiedName other = (QualifiedName) obj;
		
		return Objects.equals(this.namespace, other.namespace) &&
				Objects.equals(this.simpleName, other.simpleName);
	}	

	@Override
	public int hashCode() {
		return Objects.hash(
				getClass().getName(),
				namespace,
				simpleName
		);
	}
}
