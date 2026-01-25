package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.infrastructure.authz.domain.model.Role;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.bootstrapers.UsersBootstrapperBase;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.HashSet;
import java.util.Set;

public class ShodroneUsersBootstrapper extends UsersBootstrapperBase implements Action {

    @Override
    public boolean execute() {
        registerCRMManager("crmmanager@showdrone.com", TestDataConstants.PASSWORD1, "Crm", "Manager", "911111111");
        registerCRMCollaborator("crmcollaborator@showdrone.com", TestDataConstants.PASSWORD1, "Crm", "Collaborator", "911111111");
        registerShowDesigner("showdesigner@showdrone.com", TestDataConstants.PASSWORD1, "Show", "Designer", "911111111");
        registerDroneTech("dronetech@showdrone.com", TestDataConstants.PASSWORD1, "Drone", "Tech", "911111111");
        return true;
    }

    private void registerCRMManager(final String email, final String password,
                                        final String firstName, final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_MANAGER);

        registerUser(email, password, firstName, lastName, phoneNumber, roles);
    }

    private void registerCRMCollaborator(final String email, final String password,
                                              final String firstName, final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.CRM_COLLABORATOR);

        registerUser(email, password, firstName, lastName, phoneNumber, roles);
    }

    private void registerShowDesigner(final String email, final String password,
                                         final String firstName, final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.SHOW_DESIGNER);

        registerUser(email, password, firstName, lastName, phoneNumber, roles);
    }

    private void registerDroneTech(final String email, final String password,
                                      final String firstName, final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.DRONE_TECH);

        registerUser(email, password, firstName, lastName, phoneNumber, roles);
    }

}
