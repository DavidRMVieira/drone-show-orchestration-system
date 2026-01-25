package lapr4.presentation.customers;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.customermanagement.application.RegisterCustomerController;
import lapr4.customermanagement.domain.CustomerType;
import lapr4.customermanagement.dto.CustomerDTO;

@SuppressWarnings("java:S106")
public class RegisterCustomerUI extends AbstractUI {

    private final RegisterCustomerController controller = new RegisterCustomerController();

    @Override
    protected boolean doShow() {

        System.out.println("\n--- Customer Information ---");
        final String vatNumber = Console.readLine("VAT Number: ");
        final String firstName = Console.readLine("First Name: ");
        final String lastName = Console.readLine("Last Name: ");
        final CustomerType customerType = selectCustomerType();
        final String street = Console.readLine("Street: ");
        final String city = Console.readLine("City: ");
        final String postalCode = Console.readLine("Postal Code: ");
        final String country = Console.readLine("Country: ");

        System.out.println("\n--- Representative Information ---");
        final String repFirstName = Console.readLine("First Name: ");
        final String repLastName = Console.readLine("Last Name: ");
        final String repEmail = Console.readLine("Email: ");
        final String repPhoneNumber = Console.readLine("Phone Number: ");
        final String repPosition = Console.readLine("Position: ");
        final String repShodroneEmail = Console.readLine("Shodrone Email: ");
        final String repPassword = Console.readLine("Password: ");

        try {
            CustomerDTO registrationResult = controller.registerCustomer(
                    vatNumber, firstName, lastName, street, city, postalCode, country, customerType,
                    repFirstName, repLastName, repEmail, repPhoneNumber, repPosition, repShodroneEmail, repPassword
            );
            showRegistrationResult(registrationResult);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }    catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That VAT number or email is already in use.");
        }

        return false;
    }

    private CustomerType selectCustomerType() {
        System.out.println("Customer Types Available:");
        for (final CustomerType type : CustomerType.values()) {
            System.out.println("\t" + type.toString());
        }

        do {
            try {
                final String type = Console.readLine("Customer Type: ").toUpperCase();
                return CustomerType.valueOf(type);
            } catch (final IllegalArgumentException e) {
                System.out.println("Please try again. Enter a valid customer type.");
            }
        } while (true);
    }

    private void showRegistrationResult(CustomerDTO dto) {
        System.out.println("\n=== Registration Successful ===");

        CustomerDTOPrinter printer = new CustomerDTOPrinter();
        System.out.println(printer.header());
        printer.visit(dto);
    }

    @Override
    public String headline() {
        return "Register Customer";
    }

}
