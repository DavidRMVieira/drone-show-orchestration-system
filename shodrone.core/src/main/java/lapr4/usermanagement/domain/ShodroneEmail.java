package lapr4.usermanagement.domain;

import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.util.StringPredicates;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ShodroneEmail implements ValueObject, Comparable<ShodroneEmail> {

    private static final long serialVersionUID = 1L;

    private static final String SHODRONE_DOMAIN = "@showdrone.com";

    private String email;

    public ShodroneEmail(final String shodroneEmail) {
        Preconditions.nonEmpty(shodroneEmail, "Email address should neither be null nor empty");
        Preconditions.ensure(isValidShodroneDomain(shodroneEmail), "Invalid Shodrone Email. Must be a valid email ending with " + SHODRONE_DOMAIN);

        this.email = shodroneEmail;
    }

    protected ShodroneEmail() {
        // for ORM
    }

    public static ShodroneEmail valueOf(final String shodroneEmail) {
        return new ShodroneEmail(shodroneEmail);
    }

    private static boolean isValidShodroneDomain(final String email) {
        return StringPredicates.isEmail(email) &&
                email.toLowerCase().endsWith(SHODRONE_DOMAIN);
    }

    @Override
    public String toString() {
        return this.email;
    }

    @Override
    public int compareTo(final ShodroneEmail arg0) {
        return email.compareTo(arg0.email);
    }

}
