package lapr4.usermanagement.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.application.exceptions.UnauthorizedException;
import lapr4.usermanagement.repositories.ShodroneUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnableUserServiceTest {

    private EnableUserController subject;
    private ShodroneUserRepository repo;
    private UserManagementService userSvc;
    private AuthorizationService authz;

    @BeforeEach
    void setUp() {
        repo = mock(ShodroneUserRepository.class);
        userSvc = mock(UserManagementService.class);
        authz = mock(AuthorizationService.class);

        subject = new EnableUserController(authz, userSvc, repo);
    }


    @Test
    void allUsersRequiresAuthorization() {
        doThrow(UnauthorizedException.class).when(authz).ensureAuthenticatedUserHasAnyOf(any());

        assertThrows(UnauthorizedException.class, () -> subject.disabledUsers());
    }

}
