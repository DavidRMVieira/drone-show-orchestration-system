package lapr4.usermanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShodroneEmailTest {

    private static final String VALID_EMAIL = "user@showdrone.com";
    private static final String INVALID_EMAIL_WRONG_DOMAIN = "user@wrongdomain.com";
    private static final String INVALID_EMAIL_NO_DOMAIN = "invalidemail";
    private static final String EMPTY_EMAIL = "";
    private static final String NULL_EMAIL = null;

    @Test
    void ensureValidEmailIsAccepted() {
        final var instance = new ShodroneEmail(VALID_EMAIL);
        assertEquals(VALID_EMAIL, instance.toString());
    }

    @Test
    void ensureEmailMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneEmail(NULL_EMAIL));
    }

    @Test
    void ensureEmailMustNotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneEmail(EMPTY_EMAIL));
    }

    @Test
    void ensureEmailMustBeValidFormat() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneEmail(INVALID_EMAIL_NO_DOMAIN));
    }

    @Test
    void ensureEmailMustHaveValidShodroneDomain() {
        assertThrows(IllegalArgumentException.class, () -> new ShodroneEmail(INVALID_EMAIL_WRONG_DOMAIN));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new ShodroneEmail(VALID_EMAIL);
        final var fromValueOf = ShodroneEmail.valueOf(VALID_EMAIL);
        assertEquals(fromConstructor, fromValueOf);
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var email1 = new ShodroneEmail("a@showdrone.com");
        final var email2 = new ShodroneEmail("b@showdrone.com");
        assertEquals(-1, email1.compareTo(email2));
        assertEquals(1, email2.compareTo(email1));
        assertEquals(0, email1.compareTo(email1));
    }

}