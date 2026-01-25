package lapr4.usermanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.dto.ShodroneUserDTO;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

import java.util.Optional;

@UseCaseController
public class ListUsersController {

    private final AuthorizationService authz;
    private final ShodroneUserRepository repo;

    public ListUsersController() {
        this.authz = AuthzRegistry.authorizationService();
        this.repo = PersistenceContext.repositories().shodroneUsers();
    }

    public ListUsersController(AuthorizationService authz, ShodroneUserRepository repo) {
        // dependency injection to become more testable
        this.authz = authz;
        this.repo = repo;
    }

    public Iterable<ShodroneUserDTO> listUsers() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);

        final Iterable<ShodroneUser> users = repo.findAll();
        return ShodroneUserDTOParser.transformToDTO(users);
    }

    public Optional<ShodroneUser> findUser(final String email) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN);
        return repo.ofIdentity(ShodroneEmail.valueOf(email));
    }

}
