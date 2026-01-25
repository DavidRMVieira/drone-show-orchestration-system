package lapr4.bootstrapers;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.domain.model.Role;
import lapr4.usermanagement.application.AddUserController;
import lapr4.usermanagement.application.ListUsersController;
import lapr4.usermanagement.domain.ShodroneUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class UsersBootstrapperBase {
    private static final Logger LOGGER = LogManager.getLogger(UsersBootstrapperBase.class);

    private final AddUserController userController = new AddUserController();
    private final ListUsersController listUserController = new ListUsersController();

    public UsersBootstrapperBase() {
        super();
    }

    protected ShodroneUser registerUser(final String email, final String password, final String firstName,
                                        final String lastName, final String phoneNumber, final Set<Role> roles) {
        ShodroneUser u = null;
        try {
            u = userController.addUser(email, password, firstName, lastName, phoneNumber, roles);
            LOGGER.debug("»»» {}", email);
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            // assuming it is just a primary key violation due to the tentative
            // of inserting a duplicated user. let's just lookup that user
            u = listUserController.findUser(email).orElseThrow(() -> e);
        }
        return u;
    }

}
