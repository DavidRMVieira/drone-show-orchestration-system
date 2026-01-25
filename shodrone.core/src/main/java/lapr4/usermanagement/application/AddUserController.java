package lapr4.usermanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.Role;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.ShodroneUser;

import java.util.Set;

@UseCaseController
public class AddUserController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final RegisterShodroneUserService svc = new RegisterShodroneUserService();

    public Role[] roleTypes() {
        return ShodroneRoles.allRoles();
    }

    public ShodroneUser addUser(final String email, final String password, final String firstName,
                                final String lastName, final String phoneNumber, final Set<Role> roles) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);
        return svc.createShodroneUser(email, password, firstName, lastName, phoneNumber, roles);
    }

}
