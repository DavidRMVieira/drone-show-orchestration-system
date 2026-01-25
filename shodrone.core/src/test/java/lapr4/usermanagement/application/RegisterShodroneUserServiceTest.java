package lapr4.usermanagement.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import lapr4.usermanagement.util.ShodroneUserTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.UserManagementService;
import eapli.framework.infrastructure.authz.domain.model.*;

import lapr4.usermanagement.domain.*;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

class RegisterShodroneUserServiceTest {

    private RegisterShodroneUserService subject;
    private ShodroneUserRepository repo;
    private UserManagementService userSvc;
    private AuthorizationService authz;

    private final Set<Role> roles = Set.of(ShodroneRoles.ADMIN);
    private final String email = "test@showdrone.com";
    private final String password = "Password1";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String phone = "123456789";

    @BeforeEach
    void setUp() {
        repo = mock(ShodroneUserRepository.class);
        userSvc = mock(UserManagementService.class);
        authz = mock(AuthorizationService.class);

        subject = new RegisterShodroneUserService(authz, userSvc, repo);
    }

    @Test
    void createUserSuccessfully() {
        SystemUser systemUser = ShodroneUserTestUtil.dummyUser(email, password, firstName, lastName, ShodroneRoles.ADMIN);
        when(userSvc.registerNewUser(any(), any(), any(), any(), (Set<Role>) any(), any())).thenReturn(systemUser);
        when(repo.save(any(ShodroneUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShodroneUser user = subject.createShodroneUser(email, password, firstName, lastName, phone, roles);

        assertNotNull(user);

        verify(repo).save(any(ShodroneUser.class));
    }

    @Test
    void whenUserAlreadyExistsThenAnExceptionIsThrown() {
        SystemUser systemUser = ShodroneUserTestUtil.dummyUser(email, password, firstName, lastName, ShodroneRoles.ADMIN);
        when(userSvc.registerNewUser(any(), any(), any(), any(), (Set<Role>) any(), any())).thenReturn(systemUser);
        when(repo.save(any(ShodroneUser.class))).thenThrow(new IntegrityViolationException("Duplicate"));

        assertThrows(IntegrityViolationException.class, () -> {
            subject.createShodroneUser(email, password, firstName, lastName, phone, roles);
        });
    }

}
