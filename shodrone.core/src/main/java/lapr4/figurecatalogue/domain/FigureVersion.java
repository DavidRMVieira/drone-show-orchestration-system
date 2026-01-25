package lapr4.figurecatalogue.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class FigureVersion implements ValueObject, Comparable<FigureVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String figureVersion;

    public FigureVersion(String figureVersion) {
        Preconditions.nonEmpty(figureVersion, "Version should neither be null nor empty");

        this.figureVersion = figureVersion;
    }

    protected FigureVersion() {
        // for ORM only
    }

    public static FigureVersion valueOf(final String version) {
        return new FigureVersion(version);
    }

    @Override
    public String toString() {
        return figureVersion;
    }

    @Override
    public int compareTo(final FigureVersion other) {
        return figureVersion.compareTo(other.figureVersion);
    }
}