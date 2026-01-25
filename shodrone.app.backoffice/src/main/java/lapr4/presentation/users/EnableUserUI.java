package lapr4.presentation.users;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.usermanagement.application.EnableUserController;
import lapr4.usermanagement.dto.ShodroneUserDTO;

@SuppressWarnings("squid:S106")
public class EnableUserUI extends AbstractUI {

    private final EnableUserController controller = new EnableUserController();

    @Override
    protected boolean doShow() {
        final Iterable<ShodroneUserDTO> disabledUsers = controller.disabledUsers();

        if (!disabledUsers.iterator().hasNext()) {
            System.out.println("There are no disabled users");
        } else {
            ShodroneUserDTOPrinter printer = new ShodroneUserDTOPrinter();
            final SelectWidget<ShodroneUserDTO> selector = new SelectWidget<>(printer.header(), disabledUsers,
                    printer);
            selector.show();
            final ShodroneUserDTO selectedUser = selector.selectedElement();
            if (selectedUser != null) {
                try {
                    SystemUser user = controller.enableUser(selectedUser);
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
        return "Enable User";
    }
}
