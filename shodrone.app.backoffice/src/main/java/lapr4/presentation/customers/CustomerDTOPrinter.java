package lapr4.presentation.customers;

import eapli.framework.visitor.Visitor;
import lapr4.customermanagement.dto.CustomerDTO;

public class CustomerDTOPrinter implements Visitor<CustomerDTO> {

    @Override
    public void visit(CustomerDTO visitee) {
        System.out.printf(
                "%-20s%-25s%-45s%-15s%-10s%n",
                visitee.getVatNumber(),
                visitee.getName(),
                visitee.getAddress(),
                visitee.getState(),
                visitee.getType()
        );
    }

    public String header() {
        return String.format(
                "%-20s%-25s%-45s%-15s%-10s%n",
                "VAT", "NAME", "ADDRESS", "STATE", "TYPE"
        );
    }

}

