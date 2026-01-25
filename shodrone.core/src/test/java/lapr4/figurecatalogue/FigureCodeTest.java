package lapr4.figurecatalogue;

import lapr4.figurecatalogue.domain.FigureCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureCodeTest {

    private static final String VALID_CODE = "FIG001";
    private static final String ANOTHER_VALID_CODE = "FIG002";

    @Test
    void ensureValidFigureCodeIsCreated() {
        final var figureCode = new FigureCode(VALID_CODE);
        assertNotNull(figureCode);
        assertEquals(VALID_CODE, figureCode.toString());
    }

    @Test
    void ensureFigureCodeCannotBeNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new FigureCode(null));
        assertThrows(IllegalArgumentException.class, () -> new FigureCode(""));
    }

    @Test
    void ensureValueOfCreatesFigureCodeCorrectly() {
        final var figureCode = FigureCode.valueOf(VALID_CODE);
        assertNotNull(figureCode);
        assertEquals(VALID_CODE, figureCode.toString());
    }

    @Test
    void ensureEqualsAndHashCodeWorkCorrectly() {
        final var figureCode1 = new FigureCode(VALID_CODE);
        final var figureCode2 = new FigureCode(VALID_CODE);
        final var figureCode3 = new FigureCode(ANOTHER_VALID_CODE);

        assertEquals(figureCode1, figureCode2);
        assertNotEquals(figureCode1, figureCode3);

        assertEquals(figureCode1.hashCode(), figureCode2.hashCode());
        assertNotEquals(figureCode1.hashCode(), figureCode3.hashCode());
    }

    @Test
    void ensureCompareToWorksCorrectly() {
        final var figureCode1 = new FigureCode(VALID_CODE);
        final var figureCode2 = new FigureCode(ANOTHER_VALID_CODE);

        assertTrue(figureCode1.compareTo(figureCode2) < 0); // Assuming "FIG001" < "FIG002"
        assertTrue(figureCode2.compareTo(figureCode1) > 0); // Assuming "FIG002" > "FIG001"
        assertEquals(0, figureCode1.compareTo(new FigureCode(VALID_CODE))); // Equal codes
    }

    @Test
    void ensureToStringReturnsCode() {
        final var figureCode = new FigureCode(VALID_CODE);
        assertEquals(VALID_CODE, figureCode.toString());
    }
}

