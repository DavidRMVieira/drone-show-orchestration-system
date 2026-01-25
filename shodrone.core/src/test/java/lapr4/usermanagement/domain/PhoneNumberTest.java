package lapr4.usermanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    private static final String VALID_PHONE_WITH_PLUS = "+351912345678";
    private static final String VALID_PHONE_NO_PLUS = "912345678";
    private static final String INVALID_PHONE_TOO_SHORT = "12345";
    private static final String INVALID_PHONE_TOO_LONG = "12345678901234567890";
    private static final String INVALID_PHONE_LETTERS = "91234abcde";
    private static final String EMPTY_PHONE = "";
    private static final String NULL_PHONE = null;

    @Test
    void ensureValidPhoneWithPlusIsAccepted() {
        final var instance = new PhoneNumber(VALID_PHONE_WITH_PLUS);
        assertEquals(VALID_PHONE_WITH_PLUS, instance.toString());
    }

    @Test
    void ensureValidPhoneWithoutPlusIsAccepted() {
        final var instance = new PhoneNumber(VALID_PHONE_NO_PLUS);
        assertEquals(VALID_PHONE_NO_PLUS, instance.toString());
    }

    @Test
    void ensurePhoneMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(NULL_PHONE));
    }

    @Test
    void ensurePhoneMustNotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(EMPTY_PHONE));
    }

    @Test
    void ensurePhoneMustHaveValidLength() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(INVALID_PHONE_TOO_SHORT));
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(INVALID_PHONE_TOO_LONG));
    }

    @Test
    void ensurePhoneMustContainOnlyDigits() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(INVALID_PHONE_LETTERS));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new PhoneNumber(VALID_PHONE_NO_PLUS);
        final var fromValueOf = PhoneNumber.valueOf(VALID_PHONE_NO_PLUS);
        assertEquals(fromConstructor, fromValueOf);
    }

}
