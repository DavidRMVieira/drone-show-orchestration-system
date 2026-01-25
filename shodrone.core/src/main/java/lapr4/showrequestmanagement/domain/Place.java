package lapr4.showrequestmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Place implements ValueObject {

    private static final long serialVersionUID = 1L;

    private String name;

    public Place(final String name) {
        Preconditions.nonEmpty(name, "Place name must not be null or empty");
        this.name = name;
    }

    protected Place() {
        // for ORM
    }

    public static Place valueOf(final String name) {
        return new Place(name);
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
