package lapr4.customermanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class VAT implements ValueObject, Comparable<VAT> {

    private static final long serialVersionUID = 1L;

    private static final String VAT_FORMAT_REGEX = "^[A-Z]{2}\\w{7,11}$";
    private static final String EXPECTED_FORMAT_MESSAGE = "VAT must start with 2 uppercase letters (code), followed by 7 to 11 alphanumeric characters.";

    private String vatNumber;

    public VAT(final String vatNumber) {
        Preconditions.nonEmpty(vatNumber, "VAT number should neither be null nor empty");
        Preconditions.ensure(vatNumber.matches(VAT_FORMAT_REGEX),
                "Invalid VAT number: " + vatNumber + ". " + EXPECTED_FORMAT_MESSAGE);

        this.vatNumber = vatNumber;
    }

    protected VAT() {
        // for ORM
    }

    public static VAT valueOf(final String vatNumber) {
        return new VAT(vatNumber);
    }

    @Override
    public String toString() {
        return vatNumber;
    }

    @Override
    public int compareTo(final VAT other) {
        return vatNumber.compareTo(other.vatNumber);
    }

}
