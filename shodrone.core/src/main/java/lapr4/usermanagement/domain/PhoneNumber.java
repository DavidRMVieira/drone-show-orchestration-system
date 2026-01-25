package lapr4.usermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class PhoneNumber implements ValueObject {

    private static final long serialVersionUID = 1L;

    private String number;

    protected PhoneNumber(final String number) {
        Preconditions.nonEmpty(number, "Phone number should neither be null nor empty");
        Preconditions.ensure(isValidPhoneNumber(number), "Invalid phone number format");

        this.number = number;
    }

    protected PhoneNumber() {
        // for ORM
    }

    public static PhoneNumber valueOf(final String number) {
        return new PhoneNumber(number);
    }

    private static boolean isValidPhoneNumber(String number) {
        // Regex: optional '+' followed by 9 to 15 digits
        return number != null && number.matches("^\\+?[0-9]{9,15}$");
    }

    @Override
    public String toString() {
        return this.number;
    }

}
