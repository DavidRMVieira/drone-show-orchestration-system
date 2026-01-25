package lapr4.integrations.dronelanguageplugin.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class RegisterDroneLanguagePluginController {

    private final AuthorizationService authorizationService = AuthzRegistry.authorizationService();
    private final DroneLanguagePluginRepository repository = PersistenceContext.repositories().droneLanguagePlugins();;

    public DroneLanguagePlugin registerDroneLanguagePlugin(final String droneLanguageVersion, final String className) {
        authorizationService.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);

        final var plugin = new DroneLanguagePlugin(DroneLanguageVersion.valueOf(droneLanguageVersion), FQClassName.valueOf(className));
        return repository.save(plugin);
    }
}
