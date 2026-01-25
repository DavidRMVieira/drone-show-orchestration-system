package lapr4.figurecatalogue;

import eapli.framework.general.domain.model.Description;
import lapr4.figurecatalogue.domain.DSL;
import lapr4.figurecatalogue.domain.DSLVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DSLTest {

    private static final Description VALID_DESCRIPTION = Description.valueOf("A valid description of the DSL.");
    private static final DSLVersion VALID_DSL_VERSION = new DSLVersion("1.3.3");

    private static final Description NEW_DESCRIPTION = Description.valueOf("An updated description of the DSL.");
    private static final DSLVersion NEW_DSL_VERSION = new DSLVersion("1.0.5");

    @Test
    void ensureValidDSLIsCreated() {
        final var dsl = new DSL(VALID_DESCRIPTION, VALID_DSL_VERSION);
        assertNotNull(dsl);
        assertEquals(VALID_DESCRIPTION, dsl.description());
        assertEquals(VALID_DSL_VERSION, dsl.dslVersion());
    }

    @Test
    void ensureAllParametersAreRequired() {
        assertThrows(IllegalArgumentException.class, () -> new DSL(null, VALID_DSL_VERSION));
        assertThrows(IllegalArgumentException.class, () -> new DSL(VALID_DESCRIPTION, null));
        assertThrows(IllegalArgumentException.class, () -> new DSL(null, null));
    }

    @Test
    void ensureSameAsWorksCorrectly() {
        final var dsl1 = new DSL(VALID_DESCRIPTION, VALID_DSL_VERSION);
        final var dsl2 = new DSL(VALID_DESCRIPTION, NEW_DSL_VERSION);

        assertTrue(dsl1.sameAs(dsl1));
        assertFalse(dsl1.sameAs(dsl2));
        assertFalse(dsl1.sameAs(null));
    }

    @Test
    void ensureIdentityIsCorrect() {
        final var dsl = new DSL(VALID_DESCRIPTION, VALID_DSL_VERSION);
        assertEquals(dsl.dslVersion(),dsl.identity());
    }

    @Test
    void ensureToStringContainsRelevantInformation() {
        final var dsl = new DSL(VALID_DESCRIPTION, VALID_DSL_VERSION);
        final var toString = dsl.toString();

        assertTrue(toString.contains("description"));
        assertTrue(toString.contains("dslVersion"));
    }
}