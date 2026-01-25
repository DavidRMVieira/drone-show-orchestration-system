package lapr4.integrations.dslplugin;

import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.integrations.dslplugin.domain.DSLPlugin;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DSLPluginTest {

    private static final DSLVersion VALID_DSL_VERSION = DSLVersion.valueOf("1.0");
    private static final FQClassName VALID_FQ_CLASS_NAME = FQClassName.valueOf("lapr4.integrations.plugins.dsl.version_1_1_23.DSLPluginFormatImporter");
    private static final DSLVersion OTHER_DSL_VERSION = DSLVersion.valueOf("2.0");
    private static final FQClassName OTHER_FQ_CLASS_NAME = FQClassName.valueOf("lapr4.customermanagement.domain.Customer");

    @Test
    void ensureValidDSLPluginIsCreated() {
        final var dslPlugin = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        assertNotNull(dslPlugin);
        assertEquals(VALID_DSL_VERSION, dslPlugin.identity());
        assertEquals(VALID_FQ_CLASS_NAME, dslPlugin.className());
    }

    @Test
    void ensureNullParametersAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new DSLPlugin(null, VALID_FQ_CLASS_NAME));
        assertThrows(IllegalArgumentException.class,
                () -> new DSLPlugin(VALID_DSL_VERSION, null));
    }

    @Test
    void ensureSameAsWorksCorrectly() {
        final var dslPlugin1 = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        final var dslPlugin2 = new DSLPlugin(OTHER_DSL_VERSION, OTHER_FQ_CLASS_NAME);

        assertTrue(dslPlugin1.sameAs(dslPlugin1));
        assertFalse(dslPlugin1.sameAs(dslPlugin2));
    }

    @Test
    void ensureToStringContainsRelevantInfo() {
        final var dslPlugin = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        final var toString = dslPlugin.toString();

        assertTrue(toString.contains(VALID_DSL_VERSION.toString()));
        assertTrue(toString.contains(VALID_FQ_CLASS_NAME.toString()));
    }

    @Test
    void ensureIdentityReturnsDSLVersion() {
        final var dslPlugin = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        assertEquals(VALID_DSL_VERSION, dslPlugin.identity());
    }

    @Test
    void ensureHashCodeIsConsistentForSameObject() {
        final var dslPlugin = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        final int firstHashCode = dslPlugin.hashCode();
        final int secondHashCode = dslPlugin.hashCode();

        assertEquals(firstHashCode, secondHashCode,
                "HashCode should be consistent for the same object");
    }

    @Test
    void ensureSameAsReturnsFalseForDifferentTypes() {
        final var dslPlugin = new DSLPlugin(VALID_DSL_VERSION, VALID_FQ_CLASS_NAME);
        final String differentTypeObject = "Not a DSLPlugin";

        assertFalse(dslPlugin.sameAs(differentTypeObject),
                "sameAs should return false when comparing with different object types");
        assertFalse(dslPlugin.sameAs(null),
                "sameAs should return false when comparing with null");
    }
}