package lapr4.integrations.dronelanguageplugin;

import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneLanguageVersionTest {

    private static final String VALID_VERSION_STRING = "1.0";
    private static final String OTHER_VERSION_STRING = "2.0";

    @Test
    void ensureValidDroneLanguageVersionIsCreated() {
        final var version = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);
        assertNotNull(version);
        assertEquals(VALID_VERSION_STRING, version.toString());
    }

    @Test
    void ensureEmptyOrNullVersionIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> DroneLanguageVersion.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> DroneLanguageVersion.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> new DroneLanguageVersion(""));  // direct constructor
        assertThrows(IllegalArgumentException.class, () -> new DroneLanguageVersion(null)); // direct constructor
    }

    @Test
    void ensureToStringReturnsVersionString() {
        final var version = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);
        assertEquals(VALID_VERSION_STRING, version.toString());
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var version1 = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);
        final var version2 = DroneLanguageVersion.valueOf(OTHER_VERSION_STRING);
        final var version3 = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);

        assertTrue(version1.compareTo(version2) < 0);
        assertTrue(version2.compareTo(version1) > 0);
        assertEquals(0, version1.compareTo(version3));
    }

    @Test
    void ensureEqualsAndHashCodeAreConsistent() {
        final var version1a = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);
        final var version1b = DroneLanguageVersion.valueOf(VALID_VERSION_STRING);
        final var version2 = DroneLanguageVersion.valueOf(OTHER_VERSION_STRING);

        assertEquals(version1a, version1b);
        assertEquals(version1a.hashCode(), version1b.hashCode());

        assertNotEquals(version1a, version2);
        assertNotEquals(version1a.hashCode(), version2.hashCode());
    }

}

