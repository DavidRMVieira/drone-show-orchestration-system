package lapr4.presentation.customers;

import eapli.framework.visitor.Visitor;
import lapr4.customermanagement.dto.RepresentativeDTO;

public class RepresentativeDTOPrinter implements Visitor<RepresentativeDTO> {

    @Override
    public void visit(RepresentativeDTO visitee) {
        System.out.printf(
                "%-25s%-25s%-15s%-15s%-25s%-30s%n",
                visitee.getName(),
                visitee.getPosition(),
                visitee.getPhoneNumber(),
                visitee.getCustomerVat(),
                visitee.getEmail(),
                visitee.getShodroneEmail()
        );
    }

    public String header() {
        return String.format(
                "%-25s%-25s%-15s%-15s%-25s%-30s%n",
                "NAME", "POSITION", "PHONE", "CUSTOMER VAT", "EMAIL", "SHODRONE EMAIL"
        );
    }
}
