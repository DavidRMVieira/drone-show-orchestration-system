package lapr4.presentation.users;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.usermanagement.application.DisableUserController;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.dto.ShodroneUserDTO;

@SuppressWarnings("squid:S106")
public class DisableUserUI extends AbstractUI {

    private final DisableUserController controller = new DisableUserController();

    @Override
    protected boolean doShow() {
        final Iterable<ShodroneUserDTO> activeUsers = controller.activeUsers();

        if (!activeUsers.iterator().hasNext()) {
            System.out.println("There are no active users");
        } else {
            ShodroneUserDTOPrinter printer = new ShodroneUserDTOPrinter();
            final SelectWidget<ShodroneUserDTO> selector = new SelectWidget<>(printer.header(), activeUsers,
                    printer);
            selector.show();
            final ShodroneUserDTO selectedUser = selector.selectedElement();
            if (selectedUser != null) {
                try {
                    SystemUser user = controller.disableUser(selectedUser);
                    showUserResult(user);
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out.println(
                            "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }

        return false;
    }

    private void showUserResult(final SystemUser user) {
        System.out.printf(
                "%-35s%-20s%-20s%-15s%n",
                "EMAIL", "FIRST NAME", "LAST NAME", "STATUS"
        );
        String status = user.isActive() ? "Active" : "Inactive";
        System.out.printf(
                "%-35s%-20s%-20s%-15s%n",
                user.username(),
                user.name().firstName(),
                user.name().lastName(),
                status
        );
    }

    @Override
    public String headline() {
        return "Disable User";
    }
}
