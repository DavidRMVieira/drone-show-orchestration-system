package lapr4.integrations.dronelanguageplugin;

import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DroneLanguagePluginTest {

    private static final DroneLanguageVersion VALID_VERSION = DroneLanguageVersion.valueOf("1.0");
    private static final FQClassName VALID_CLASS_NAME = FQClassName.valueOf("lapr4.integrations.plugins.drone.DroneLanguagePluginClass");
    private static final DroneLanguageVersion OTHER_VERSION = DroneLanguageVersion.valueOf("2.0");
    private static final FQClassName OTHER_CLASS_NAME = FQClassName.valueOf("lapr4.other.domain.OtherDroneClass");

    @Test
    void ensureValidDroneLanguagePluginIsCreated() {
        final var plugin = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        assertNotNull(plugin);
        assertEquals(VALID_VERSION, plugin.identity());
        assertEquals(VALID_CLASS_NAME, plugin.className());
    }

    @Test
    void ensureNullParametersAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new DroneLanguagePlugin(null, VALID_CLASS_NAME));
        assertThrows(IllegalArgumentException.class,
                () -> new DroneLanguagePlugin(VALID_VERSION, null));
    }

    @Test
    void ensureSameAsWorksCorrectly() {
        final var plugin1 = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        final var plugin2 = new DroneLanguagePlugin(OTHER_VERSION, OTHER_CLASS_NAME);

        assertTrue(plugin1.sameAs(plugin1)); // mesmo objeto
        assertFalse(plugin1.sameAs(plugin2)); // diferentes IDs (mas como ID é null, vamos testar igualdade por ID nulo)
        assertFalse(plugin1.sameAs(null));
        assertFalse(plugin1.sameAs("not a plugin"));
    }

    @Test
    void ensureToStringContainsRelevantInfo() {
        final var plugin = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        final var toString = plugin.toString();

        assertTrue(toString.contains(VALID_VERSION.toString()));
        assertTrue(toString.contains(VALID_CLASS_NAME.toString()));
    }

    @Test
    void ensureIdentityReturnsCorrectVersion() {
        final var plugin = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        assertEquals(VALID_VERSION, plugin.identity());
    }

    @Test
    void ensureHashCodeIsConsistent() {
        final var plugin = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        final var hashCode1 = plugin.hashCode();
        final var hashCode2 = plugin.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void ensureClassNameIsCorrectlyReturned() {
        final var plugin = new DroneLanguagePlugin(VALID_VERSION, VALID_CLASS_NAME);
        assertEquals(VALID_CLASS_NAME, plugin.className());
    }

}

