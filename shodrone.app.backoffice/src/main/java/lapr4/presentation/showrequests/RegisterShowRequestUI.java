package lapr4.presentation.showrequests;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.presentation.customers.CustomerDTOPrinter;
import lapr4.showrequestmanagement.application.RegisterShowRequestController;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

import java.util.Date;

@SuppressWarnings("java:S106")
public class RegisterShowRequestUI extends AbstractUI {

    private final RegisterShowRequestController controller = new RegisterShowRequestController();

    @Override
    protected boolean doShow() {

        final String place = Console.readLine("Place: ");
        final Date date = Console.readDate("Date: ");
        final int duration = Console.readInteger("Duration: ");

        Iterable<CustomerDTO> customers = controller.listCustomer();

        if (!customers.iterator().hasNext()) {
            System.out.println("There are no customers");
        } else {
            CustomerDTOPrinter printer = new CustomerDTOPrinter();
            final SelectWidget<CustomerDTO> selector = new SelectWidget<>(printer.header(), customers, printer);
            selector.show();
            final CustomerDTO selectedCustomer = selector.selectedElement();

            if (selectedCustomer != null) {
                try {
                    ShowRequestDTO registrationResult = controller.registerShowRequest(place, date, duration, selectedCustomer);
                    showRegistrationResult(registrationResult);
                } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
                    System.out.println("That id is already in use.");
                }
            }
        }
        return false;
    }

    private void showRegistrationResult(ShowRequestDTO showRequest) {
        System.out.println("\n=== Registration Successful ===");

        ShowRequestDTOPrinter printer = new ShowRequestDTOPrinter();
        System.out.println(printer.header());
        printer.visit(showRequest);
    }

    @Override
    public String headline() {
        return "Register Show Request";
    }

}
