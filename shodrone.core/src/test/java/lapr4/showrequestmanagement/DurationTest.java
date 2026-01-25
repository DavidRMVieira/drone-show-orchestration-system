package lapr4.showrequestmanagement;

import lapr4.showrequestmanagement.domain.Duration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DurationTest {

    private static final int VALID_MINUTES = 90;
    private static final int INVALID_NEGATIVE_MINUTES = -30;
    private static final int INVALID_ZERO_MINUTES = 0;

    @Test
    void ensureValidDurationIsAccepted() {
        final var duration = new Duration(VALID_MINUTES);
        assertEquals("90 minutes", duration.toString());
    }

    @Test
    void ensureDurationMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> new Duration(INVALID_NEGATIVE_MINUTES));
        assertThrows(IllegalArgumentException.class, () -> new Duration(INVALID_ZERO_MINUTES));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new Duration(VALID_MINUTES);
        final var fromValueOf = Duration.valueOf(VALID_MINUTES);
        assertEquals(fromConstructor, fromValueOf);
    }

    @Test
    void ensureInMinutesReturnsCorrectValue() {
        final var duration = new Duration(VALID_MINUTES);
        assertEquals(VALID_MINUTES, duration.inMinutes());
    }
}

