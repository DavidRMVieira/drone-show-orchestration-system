package lapr4.presentation.users;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import lapr4.usermanagement.application.ListUsersController;
import lapr4.usermanagement.dto.ShodroneUserDTO;

@SuppressWarnings({ "squid:S106" })
public class ListUsersUI extends AbstractListUI<ShodroneUserDTO> {

    private final ListUsersController controller = new ListUsersController();

    @Override
    public String headline() {
        return "List Users";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<ShodroneUserDTO> elements() {
        return controller.listUsers();
    }

    @Override
    protected Visitor<ShodroneUserDTO> elementPrinter() {
        return new ShodroneUserDTOPrinter();
    }

    @Override
    protected String elementName() {
        return "User";
    }

    @Override
    protected String listHeader() {
        return String.format(
                "#  %-35s%-20s%-20s%-20s%-15s",
                "EMAIL", "FIRST NAME", "LAST NAME", "PHONE", "STATUS"
        ) + "\n";
    }

}
