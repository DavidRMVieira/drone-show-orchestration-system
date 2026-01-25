package lapr4.integrations.sharedkernel;

import lapr4.integrations.sharedkernel.domain.FQClassName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FQClassNameTest {

    private static final String VALID_FQ_CLASS_NAME = "lapr4.integrations.plugins.dsl.version_1_1_23.DSLPluginFormatImporter";
    private static final String INVALID_FQ_CLASS_NAME_EMPTY = "";
    private static final String INVALID_FQ_CLASS_NAME_START_NUMBER = "1lapr4.showproposal.Show";
    private static final String INVALID_FQ_CLASS_NAME_SPECIAL_CHAR = "lapr4.show-proposal.Show";

    @Test
    void ensureValidFQClassNameIsAccepted() {
        final var fqClassName = FQClassName.valueOf(VALID_FQ_CLASS_NAME);
        assertEquals(VALID_FQ_CLASS_NAME, fqClassName.toString());
    }

    @Test
    void ensureEmptyClassNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> FQClassName.valueOf(INVALID_FQ_CLASS_NAME_EMPTY));
    }

    @Test
    void ensureClassNameStartingWithNumberIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> FQClassName.valueOf(INVALID_FQ_CLASS_NAME_START_NUMBER));
    }

    @Test
    void ensureClassNameWithSpecialCharsIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> FQClassName.valueOf(INVALID_FQ_CLASS_NAME_SPECIAL_CHAR));
    }

    @Test
    void ensureValueOfReturnsValidInstance() {
        final var fqClassName = FQClassName.valueOf(VALID_FQ_CLASS_NAME);
        assertNotNull(fqClassName);
        assertEquals(VALID_FQ_CLASS_NAME, fqClassName.toString());
    }

}