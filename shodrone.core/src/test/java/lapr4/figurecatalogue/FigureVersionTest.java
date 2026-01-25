package lapr4.figurecatalogue;

import lapr4.figurecatalogue.domain.FigureVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureVersionTest {

    private static final String VALID_VERSION = "1.0.0";
    private static final String ANOTHER_VALID_VERSION = "2.0.0";
    private static final String EMPTY_VERSION = "";
    private static final String NULL_VERSION = null;

    @Test
    void ensureValidFigureVersionIsCreated() {
        final var figureVersion = new FigureVersion(VALID_VERSION);
        assertNotNull(figureVersion);
        assertEquals(VALID_VERSION, figureVersion.toString());
    }

    @Test
    void ensureVersionCannotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new FigureVersion(NULL_VERSION));
        assertThrows(IllegalArgumentException.class, () -> new FigureVersion(EMPTY_VERSION));
    }

    @Test
    void ensureValueOfCreatesEquivalentInstance() {
        final var figureVersion1 = new FigureVersion(VALID_VERSION);
        final var figureVersion2 = FigureVersion.valueOf(VALID_VERSION);
        assertEquals(figureVersion1, figureVersion2);
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var figureVersion1 = new FigureVersion(VALID_VERSION);
        final var figureVersion2 = new FigureVersion(ANOTHER_VALID_VERSION);

        assertTrue(figureVersion1.compareTo(figureVersion2) < 0);
        assertTrue(figureVersion2.compareTo(figureVersion1) > 0);
        assertEquals(0, figureVersion1.compareTo(new FigureVersion(VALID_VERSION)));
    }

    @Test
    void ensureToStringReturnsCorrectValue() {
        final var figureVersion = new FigureVersion(VALID_VERSION);
        assertEquals(VALID_VERSION, figureVersion.toString());
    }
}
