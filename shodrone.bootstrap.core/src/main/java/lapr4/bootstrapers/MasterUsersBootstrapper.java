package lapr4.bootstrapers;

import eapli.framework.actions.Action;
import eapli.framework.infrastructure.authz.domain.model.Role;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.HashSet;
import java.util.Set;

public class MasterUsersBootstrapper extends UsersBootstrapperBase implements Action {

    @Override
    public boolean execute() {
        registerAdmin("jane.doe@showdrone.com", TestDataConstants.PASSWORD1, "Jane", "Doe Admin",
                "911111111");
        return true;
    }

    private void registerAdmin(final String email, final String password, final String firstName,
            final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.ADMIN);

        registerUser(email, password, firstName, lastName, phoneNumber, roles);
    }
}
