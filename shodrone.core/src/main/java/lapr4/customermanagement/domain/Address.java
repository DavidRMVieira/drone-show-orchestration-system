package lapr4.customermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class Address implements ValueObject {

    private static final long serialVersionUID = 1L;

    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address(final String street, final String city, final String postalCode, final String country) {
        Preconditions.nonEmpty(street, "Street should neither be null nor empty");
        Preconditions.nonEmpty(city, "City should neither be null nor empty");
        Preconditions.nonEmpty(postalCode, "Postal code should neither be null nor empty");
        Preconditions.nonEmpty(country, "Country should neither be null nor empty");

        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    protected Address() {
        // for ORM
    }

    public static Address valueOf(final String street, final String city, final String postalCode, final String country) {
        return new Address(street, city, postalCode, country);
    }

    public String street() {
        return street;
    }

    public String city() {
        return city;
    }

    public String postalCode() {
        return postalCode;
    }

    public String country() {
        return country;
    }

    @Override
    public String toString() {
        return street + ", " + city + ", " + postalCode + ", " + country;
    }
}
