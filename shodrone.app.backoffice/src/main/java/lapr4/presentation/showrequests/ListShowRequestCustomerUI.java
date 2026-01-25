package lapr4.presentation.showrequests;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.presentation.customers.CustomerDTOPrinter;
import lapr4.showrequestmanagement.application.ListShowRequestCustomerController;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

@SuppressWarnings("java:S106")
public class ListShowRequestCustomerUI extends AbstractUI {

    private final ListShowRequestCustomerController controller = new ListShowRequestCustomerController();

    @Override
    protected boolean doShow() {

        Iterable<CustomerDTO> customers = controller.listCustomer();

        if (!customers.iterator().hasNext()) {
            System.out.println("There are no customers");
        } else {
            CustomerDTOPrinter printer = new CustomerDTOPrinter();
            final SelectWidget<CustomerDTO> selector = new SelectWidget<>(printer.header(), customers, printer);
            selector.show();
            final CustomerDTO selectedCustomer = selector.selectedElement();
            if (selectedCustomer != null) {
                Iterable<ShowRequestDTO> listShowRequestCustomer = controller.listShowRequestCustomer(selectedCustomer);
                showListShowRequestCustomer(listShowRequestCustomer);
            }
        }
        return false;
    }

    private void showListShowRequestCustomer(Iterable<ShowRequestDTO> listShowRequestCustomer) {
        System.out.println("\n=== List all show requests of a customer ===\n");

        ShowRequestDTOPrinter printer = new ShowRequestDTOPrinter();
        System.out.println(printer.header());
        for (ShowRequestDTO showRequest : listShowRequestCustomer){
            printer.visit(showRequest);
        }
    }

    @Override
    public String headline() {
        return "List Show Request Customer";
    }

}
