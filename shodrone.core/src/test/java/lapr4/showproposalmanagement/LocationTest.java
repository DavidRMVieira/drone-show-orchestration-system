package lapr4.showproposalmanagement;

import lapr4.showproposalmanagement.domain.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private static final double VALID_LATITUDE = 41.1579;
    private static final double VALID_LONGITUDE = -8.6291;

    private static final double INVALID_LATITUDE_LOW = -91.0;
    private static final double INVALID_LATITUDE_HIGH = 91.0;
    private static final double INVALID_LONGITUDE_LOW = -181.0;
    private static final double INVALID_LONGITUDE_HIGH = 181.0;

    @Test
    void ensureValidLocationIsAccepted() {
        final var location = new Location(VALID_LATITUDE, VALID_LONGITUDE);
        assertEquals(VALID_LATITUDE, location.latitude());
        assertEquals(VALID_LONGITUDE, location.longitude());
    }

    @Test
    void ensureLatitudeMustBeWithinValidRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(INVALID_LATITUDE_LOW, VALID_LONGITUDE));
        assertThrows(IllegalArgumentException.class, () -> new Location(INVALID_LATITUDE_HIGH, VALID_LONGITUDE));
    }

    @Test
    void ensureLongitudeMustBeWithinValidRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(VALID_LATITUDE, INVALID_LONGITUDE_LOW));
        assertThrows(IllegalArgumentException.class, () -> new Location(VALID_LATITUDE, INVALID_LONGITUDE_HIGH));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new Location(VALID_LATITUDE, VALID_LONGITUDE);
        final var fromValueOf = Location.valueOf(VALID_LATITUDE, VALID_LONGITUDE);
        assertEquals(fromConstructor, fromValueOf);
    }

    @Test
    void ensureLatitudeReturnsCorrectValue() {
        final var location = new Location(VALID_LATITUDE, VALID_LONGITUDE);
        assertEquals(VALID_LATITUDE, location.latitude());
    }

    @Test
    void ensureLongitudeReturnsCorrectValue() {
        final var location = new Location(VALID_LATITUDE, VALID_LONGITUDE);
        assertEquals(VALID_LONGITUDE, location.longitude());
    }
}
