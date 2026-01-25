package lapr4.showrequestmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Duration implements ValueObject {

    private static final long serialVersionUID = 1L;

    private int minutes;

    public Duration(final int minutes) {
        Preconditions.ensure(minutes > 0, "Duration must be a positive value");
        this.minutes = minutes;
    }

    protected Duration() {
        // for ORM
    }

    public static Duration valueOf(final int minutes) {
        return new Duration(minutes);
    }

    public int inMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return minutes + " minutes";
    }
    
}
