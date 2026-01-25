package lapr4.figurecatalogue.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class DSLVersion implements ValueObject, Comparable<DSLVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String dslVersion;

    public DSLVersion(String dslVersion) {
        Preconditions.nonEmpty(dslVersion, "Version should neither be null nor empty");

        this.dslVersion = dslVersion;
    }

    protected DSLVersion() {
        // for ORM only
    }

    public static DSLVersion valueOf(final String version) {
        return new DSLVersion(version);
    }

    @Override
    public String toString() {
        return dslVersion;
    }

    @Override
    public int compareTo(final DSLVersion other) {
        return dslVersion.compareTo(other.dslVersion);
    }
}