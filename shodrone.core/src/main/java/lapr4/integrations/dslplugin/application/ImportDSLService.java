package lapr4.integrations.dslplugin.application;

import eapli.framework.application.ApplicationService;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dslplugin.domain.DSLPlugin;
import lapr4.integrations.dslplugin.dto.DSLDescription;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;
import lapr4.usermanagement.domain.ShodroneRoles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@ApplicationService
public class ImportDSLService {

    private static final Logger LOGGER = LogManager.getLogger(ImportDSLService.class);
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final DSLPluginRepository repo = PersistenceContext.repositories().dslPlugins();;

    public DSLDescription validateDSL(final String filename, final String dslVersion) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.DRONE_TECH);

        Optional<DSLPlugin> plugin = repo.ofIdentity(DSLVersion.valueOf(dslVersion));
        if (plugin.isEmpty()) {
            throw new IllegalArgumentException("No plugin registered for DSL version: " + dslVersion);
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

    private DSLImporter buildImporter(String className) {
        try {
            return (DSLImporter) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Unable to dynamically load the Plugin!", ex);
            throw new IllegalStateException("Unable to dynamically load the Plugin: " + className, ex);
        }
    }

}
