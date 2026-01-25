package lapr4.showrequestmanagement;

import lapr4.showrequestmanagement.domain.Place;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTest {

    private static final String VALID_NAME = "Auditorium A";
    private static final String EMPTY_NAME = "";
    private static final String NULL_NAME = null;

    @Test
    void ensureValidPlaceIsAccepted() {
        final var place = new Place(VALID_NAME);
        assertEquals("Auditorium A", place.toString());
    }

    @Test
    void ensurePlaceNameMustNotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Place(NULL_NAME));
        assertThrows(IllegalArgumentException.class, () -> new Place(EMPTY_NAME));
    }

    @Test
    void ensureValueOfReturnsSameAsConstructor() {
        final var fromConstructor = new Place(VALID_NAME);
        final var fromValueOf = Place.valueOf(VALID_NAME);
        assertEquals(fromConstructor, fromValueOf);
    }

    @Test
    void ensureNameReturnsCorrectValue() {
        final var place = new Place(VALID_NAME);
        assertEquals(VALID_NAME, place.name());
    }
}

