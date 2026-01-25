package lapr4.customermanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private static final String VALID_STREET = "Rua das Flores";
    private static final String VALID_CITY = "Porto";
    private static final String VALID_POSTAL_CODE = "4000-123";
    private static final String VALID_COUNTRY = "Portugal";

    private static final String EMPTY = "";
    private static final String NULL = null;

    @Test
    void ensureValidAddressIsAccepted() {
        final var address = new Address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, VALID_COUNTRY);
        assertEquals("Rua das Flores, Porto, 4000-123, Portugal", address.toString());
    }

    @Test
    void ensureStreetMustNotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Address(NULL, VALID_CITY, VALID_POSTAL_CODE, VALID_COUNTRY));
        assertThrows(IllegalArgumentException.class, () -> new Address(EMPTY, VALID_CITY, VALID_POSTAL_CODE, VALID_COUNTRY));
    }

    @Test
    void ensureCityMustNotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, NULL, VALID_POSTAL_CODE, VALID_COUNTRY));
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, EMPTY, VALID_POSTAL_CODE, VALID_COUNTRY));
    }

    @Test
    void ensurePostalCodeMustNotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, VALID_CITY, NULL, VALID_COUNTRY));
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, VALID_CITY, EMPTY, VALID_COUNTRY));
    }

    @Test
    void ensureCountryMustNotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, NULL));
        assertThrows(IllegalArgumentException.class, () -> new Address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, EMPTY));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new Address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, VALID_COUNTRY);
        final var fromValueOf = Address.valueOf(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, VALID_COUNTRY);
        assertEquals(fromConstructor, fromValueOf);
    }

}
