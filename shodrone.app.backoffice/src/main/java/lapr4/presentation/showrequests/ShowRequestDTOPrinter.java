package lapr4.presentation.showrequests;

import eapli.framework.visitor.Visitor;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

public class ShowRequestDTOPrinter implements Visitor<ShowRequestDTO> {

    @Override
    public void visit(ShowRequestDTO visitee) {
        System.out.printf(
                "%-10s%-20s%-30s%-20s%-20s%-25s%n",
                visitee.getId(),
                visitee.getPlace(),
                visitee.getDate(),
                visitee.getDuration(),
                visitee.getState(),
                visitee.getCustomer()
        );
    }

    public String header() {
        return String.format(
                "%-10s%-20s%-30s%-20s%-20s%-25s%n",
                "ID", "PLACE", "DATE", "DURATION", "STATE", "CUSTOMER (VAT)"
        );
    }

}
