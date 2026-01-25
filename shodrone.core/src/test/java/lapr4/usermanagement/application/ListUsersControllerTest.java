package lapr4.usermanagement.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.exceptions.UnauthorizedException;
import lapr4.usermanagement.domain.PhoneNumber;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.dto.ShodroneUserDTO;
import lapr4.usermanagement.repositories.ShodroneUserRepository;
import lapr4.usermanagement.util.ShodroneUserTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListUsersControllerTest {

    private ListUsersController subject;
    private ShodroneUserRepository repo;
    private AuthorizationService authz;

    private final String email = "test@showdrone.com";
    private final String password = "Password1";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String phone = "123456789";

    @BeforeEach
    void setUp() {
        repo = mock(ShodroneUserRepository.class);
        authz = mock(AuthorizationService.class);

        subject = new ListUsersController(authz, repo);
    }

    @Test
    void allUsersRequiresAuthorization() {
        doThrow(UnauthorizedException.class).when(authz).ensureAuthenticatedUserHasAnyOf(any());

        assertThrows(UnauthorizedException.class, () -> subject.listUsers());
        assertThrows(UnauthorizedException.class, () -> subject.findUser(email));
    }

    @Test
    void findUserReturnsUserWhenExists() {
        ShodroneUser user = new ShodroneUser(ShodroneUserTestUtil.dummyUser(email, password, firstName, lastName, ShodroneRoles.ADMIN),
                ShodroneEmail.valueOf(email), PhoneNumber.valueOf(phone));
        when(repo.ofIdentity(any())).thenReturn(Optional.of(user));

        Optional<ShodroneUser> result = subject.findUser(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findUserReturnsEmptyWhenNotExists() {
        when(repo.ofIdentity(any())).thenReturn(Optional.empty());

        Optional<ShodroneUser> result = subject.findUser(email);

        assertTrue(result.isEmpty());
    }

}
