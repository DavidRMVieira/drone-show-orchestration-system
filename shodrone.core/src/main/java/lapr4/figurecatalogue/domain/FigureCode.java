package lapr4.figurecatalogue.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class FigureCode implements ValueObject, Comparable<FigureCode> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code;

    public FigureCode(String code) {
        Preconditions.nonEmpty(code, "Code should neither be null nor empty");

        this.code = code;
    }

    protected FigureCode() {
        // for ORM only
    }

    public static FigureCode valueOf(final String code) {
        return new FigureCode(code);
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public int compareTo(final FigureCode other) {
        return code.compareTo(other.code);
    }
}
