package just.forward.api.model;

public abstract class ProblemElement {
	public abstract void accept(ModelVisitor visitor);
}
