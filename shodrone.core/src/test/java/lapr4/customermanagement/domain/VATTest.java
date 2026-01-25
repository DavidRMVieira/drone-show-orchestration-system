package lapr4.customermanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VATTest {

    private static final String VALID_VAT_SHORT = "PT1234567";
    private static final String VALID_VAT_LONG = "DE12345678901";
    private static final String INVALID_VAT_NO__CODE = "123456789";
    private static final String INVALID_VAT_TOO_SHORT = "PT123";
    private static final String INVALID_VAT_TOO_LONG = "PT123456789012345";
    private static final String INVALID_VAT_WRONG_FORMAT = "123PT45678";
    private static final String INVALID_VAT_LOWERCASE = "pt123456789";
    private static final String EMPTY_VAT = "";
    private static final String NULL_VAT = null;

    @Test
    void ensureValidVATShortIsAccepted() {
        final var instance = new VAT(VALID_VAT_SHORT);
        assertEquals(VALID_VAT_SHORT, instance.toString());
    }

    @Test
    void ensureValidVATLongIsAccepted() {
        final var instance = new VAT(VALID_VAT_LONG);
        assertEquals(VALID_VAT_LONG, instance.toString());
    }

    @Test
    void ensureVATMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new VAT(NULL_VAT));
    }

    @Test
    void ensureVATMustNotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new VAT(EMPTY_VAT));
    }

    @Test
    void ensureVATMustFollowFormat() {
        assertThrows(IllegalArgumentException.class, () -> new VAT(INVALID_VAT_NO__CODE));
        assertThrows(IllegalArgumentException.class, () -> new VAT(INVALID_VAT_TOO_SHORT));
        assertThrows(IllegalArgumentException.class, () -> new VAT(INVALID_VAT_TOO_LONG));
        assertThrows(IllegalArgumentException.class, () -> new VAT(INVALID_VAT_WRONG_FORMAT));
        assertThrows(IllegalArgumentException.class, () -> new VAT(INVALID_VAT_LOWERCASE));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new VAT(VALID_VAT_SHORT);
        final var fromValueOf = VAT.valueOf(VALID_VAT_SHORT);
        assertEquals(fromConstructor, fromValueOf);
    }

    @Test
    void ensureCompareToWorksProperly() {
        VAT vat1 = new VAT("PT123456789");
        VAT vat2 = new VAT("PT223456789");
        assertTrue(vat1.compareTo(vat2) < 0);
        assertTrue(vat2.compareTo(vat1) > 0);
        assertEquals(0, vat1.compareTo(new VAT("PT123456789")));
    }

}
