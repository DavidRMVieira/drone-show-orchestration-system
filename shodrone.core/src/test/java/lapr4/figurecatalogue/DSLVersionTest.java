package lapr4.figurecatalogue;

import lapr4.figurecatalogue.domain.DSLVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DSLVersionTest {

    private static final String VALID_VERSION = "1.0.0";
    private static final String ANOTHER_VALID_VERSION = "2.0.0";
    private static final String EMPTY_VERSION = "";
    private static final String NULL_VERSION = null;

    @Test
    void ensureValidDSLVersionIsCreated() {
        final var dslVersion = new DSLVersion(VALID_VERSION);
        assertNotNull(dslVersion);
        assertEquals(VALID_VERSION, dslVersion.toString());
    }

    @Test
    void ensureVersionCannotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new DSLVersion(NULL_VERSION));
        assertThrows(IllegalArgumentException.class, () -> new DSLVersion(EMPTY_VERSION));
    }

    @Test
    void ensureValueOfCreatesEquivalentInstance() {
        final var dslVersion1 = new DSLVersion(VALID_VERSION);
        final var dslVersion2 = DSLVersion.valueOf(VALID_VERSION);
        assertEquals(dslVersion1, dslVersion2);
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var dslVersion1 = new DSLVersion(VALID_VERSION);
        final var dslVersion2 = new DSLVersion(ANOTHER_VALID_VERSION);

        assertTrue(dslVersion1.compareTo(dslVersion2) < 0);
        assertTrue(dslVersion2.compareTo(dslVersion1) > 0);
        assertEquals(0, dslVersion1.compareTo(new DSLVersion(VALID_VERSION)));
    }

    @Test
    void ensureToStringReturnsCorrectValue() {
        final var dslVersion = new DSLVersion(VALID_VERSION);
        assertEquals(VALID_VERSION, dslVersion.toString());
    }
}
