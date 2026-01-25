package lapr4.integrations.dslplugin.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.integrations.dslplugin.dto.DSLDescription;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.io.*;

@UseCaseController
public class ValidateDSLDescriptionController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ImportDSLService importDSLService = new ImportDSLService();

    public DSLDescription validateDSL(final String dslVersion, final String filename) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);
        return importDSLService.validateDSL(filename, dslVersion);
    }

}