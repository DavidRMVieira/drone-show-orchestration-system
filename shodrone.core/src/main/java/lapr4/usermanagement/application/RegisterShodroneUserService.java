package lapr4.usermanagement.application;

import eapli.framework.application.ApplicationService;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.time.util.CurrentTimeCalendars;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.PhoneNumber;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

import java.util.Set;

@ApplicationService
public class RegisterShodroneUserService {

    private final AuthorizationService authz;
    private final UserManagementService userSvc;
    private final ShodroneUserRepository repo;

    public RegisterShodroneUserService() {
        this.authz = AuthzRegistry.authorizationService();
        this.userSvc = AuthzRegistry.userService();
        this.repo = PersistenceContext.repositories().shodroneUsers();
    }

    public RegisterShodroneUserService(TransactionalContext txCtx) {
        this.authz = AuthzRegistry.authorizationService();
        this.userSvc = AuthzRegistry.userService();
        this.repo = PersistenceContext.repositories().shodroneUsers(txCtx);
    }

    public RegisterShodroneUserService(AuthorizationService authz, UserManagementService userSvc, ShodroneUserRepository repo) {
        // dependency injection to become more testable
        this.authz = authz;
        this.userSvc = userSvc;
        this.repo = repo;
    }

    public ShodroneUser createShodroneUser(final String email, final String password, final String firstName,
                                           final String lastName, final String phoneNumber, final Set<Role> roles) {

        SystemUser systemUser = userSvc.registerNewUser(email, password, firstName,
                lastName, roles, CurrentTimeCalendars.now());

        ShodroneUser newUser = new ShodroneUser(systemUser, ShodroneEmail.valueOf(email), PhoneNumber.valueOf(phoneNumber));

        return repo.save(newUser);
    }

}
