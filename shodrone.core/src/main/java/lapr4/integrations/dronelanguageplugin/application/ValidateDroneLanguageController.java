package lapr4.integrations.dronelanguageplugin.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dronelanguageplugin.dto.DroneLanguage;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;
import lapr4.usermanagement.domain.ShodroneRoles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@UseCaseController
public class ValidateDroneLanguageController {

    private static final Logger LOGGER = LogManager.getLogger(ValidateDroneLanguageController.class);
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final DroneLanguagePluginRepository pluginRepository = PersistenceContext.repositories().droneLanguagePlugins();

    public DroneLanguage validateDroneLanguage(final String droneLanguageVersion, final String filename) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);

        Optional<DroneLanguagePlugin> plugin = pluginRepository.ofIdentity(DroneLanguageVersion.valueOf(droneLanguageVersion));
        if (plugin.isEmpty()) {
            throw new IllegalArgumentException("No plugin registered for Drone Language version: " + droneLanguageVersion);
        }

        InputStream content = null;
        try {
            content = inputStreamFromResourceOrFile(filename);
            final var className = plugin.get().className().toString();
            final var importer = buildImporter(className);

            return importer.importFrom(content);

        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (final IOException e) {
                    LOGGER.error("Error closing the file {}", filename);
                }
            }
        }
    }

    private InputStream inputStreamFromResourceOrFile(String filename) throws FileNotFoundException {
        InputStream content;
        final var classLoader = this.getClass().getClassLoader();
        final var resource = classLoader.getResource(filename);
        if (resource != null) {
            final var file = new File(resource.getFile());
            content = new FileInputStream(file);
        } else {
            filename = "files/" + filename;
            content = new FileInputStream(filename);
        }
        return content;
    }

    private DroneLanguageImporter buildImporter(String className) {
        try {
            return (DroneLanguageImporter) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Unable to dynamically load the Plugin", ex);
            throw new IllegalStateException("Unable to dynamically load the Plugin: " + className, ex);
        }
    }

}
