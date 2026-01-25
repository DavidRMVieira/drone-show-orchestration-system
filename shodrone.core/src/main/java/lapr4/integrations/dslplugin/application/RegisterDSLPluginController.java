package lapr4.integrations.dslplugin.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dslplugin.domain.DSLPlugin;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class RegisterDSLPluginController {

    private final AuthorizationService authorizationService = AuthzRegistry.authorizationService();
    private final DSLPluginRepository repository = PersistenceContext.repositories().dslPlugins();;

    public DSLPlugin registerDSLPlugin(final String dslVersion, final String className) {
        authorizationService.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);

        final var plugin = new DSLPlugin(DSLVersion.valueOf(dslVersion), FQClassName.valueOf(className));
        return repository.save(plugin);
    }
}
