package lapr4.usermanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.dto.ShodroneUserDTO;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

@UseCaseController
public class DisableUserController {

    private final AuthorizationService authz;
    private final UserManagementService userSvc;
    private final ShodroneUserRepository repo;

    public DisableUserController() {
        this.authz = AuthzRegistry.authorizationService();
        this.userSvc = AuthzRegistry.userService();
        this.repo = PersistenceContext.repositories().shodroneUsers();
    }

    public DisableUserController(AuthorizationService authz, UserManagementService userSvc, ShodroneUserRepository repo) {
        // dependency injection to become more testable
        this.authz = authz;
        this.userSvc = userSvc;
        this.repo = repo;
    }

    public Iterable<ShodroneUserDTO> activeUsers() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        final Iterable<ShodroneUser> users = repo.findAllActive();
        return ShodroneUserDTOParser.transformToDTO(users);
    }

    public SystemUser disableUser(final ShodroneUserDTO shodroneUserDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        ShodroneUser shodroneUser = new ShodroneUserDTOParser(repo).valueOf(shodroneUserDTO);
        return userSvc.deactivateUser(shodroneUser.user());
    }

}
