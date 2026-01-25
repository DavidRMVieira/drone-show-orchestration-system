package lapr4.integrations.sharedkernel.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class FQClassName implements ValueObject, Comparable<FQClassName> {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String CLASS_FORMAT = "([a-zA-Z_$][\\w$]*\\.)*([A-Z][\\w$]*)";

    private String fqClassName;

    protected FQClassName(final String name) {
        Preconditions.nonEmpty(name, "Fully qualified class name must not be empty.");
        Preconditions.ensure(name.matches(CLASS_FORMAT), "Invalid fully qualified class name format.");

        this.fqClassName = name;
    }

    protected FQClassName() {
        // for ORM
    }

    public static FQClassName valueOf(final String fqClassName) {
        return new FQClassName(fqClassName);
    }

    @Override
    public String toString() {
        return fqClassName;
    }

    @Override
    public int compareTo(final FQClassName o) {
        return fqClassName.compareTo(o.fqClassName);
    }

}
