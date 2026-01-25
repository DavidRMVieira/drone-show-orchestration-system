package lapr4.presentation.users;

import eapli.framework.visitor.Visitor;
import lapr4.usermanagement.dto.ShodroneUserDTO;

@SuppressWarnings({ "squid:S106" })
public class ShodroneUserDTOPrinter implements Visitor<ShodroneUserDTO> {

    @Override
    public void visit(final ShodroneUserDTO visitee) {
        System.out.printf(
                "%-35s%-40s%-20s%-15s%n",
                visitee.getEmail(),
                visitee.getName(),
                visitee.getPhoneNumber(),
                visitee.isActive() ? "Active" : "Inactive"
        );
    }

    public String header() {
        return String.format(
                "%-35s%-40s%-20s%-15s%n",
                "EMAIL", "NAME", "PHONE", "STATUS"
        );
    }

}
