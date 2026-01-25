package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lapr4.customermanagement.application.RegisterCustomerController;
import lapr4.customermanagement.domain.CustomerType;

public class CustomerBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(CustomerBootstrapper.class);

    @Override
    public boolean execute() {
        registerCustomer(TestDataConstants.CUSTOMER_VAT_1, "John", "Doe", "Main Street", "Lisbon", "1000-000", "Portugal",
                CustomerType.REGULAR, "Jane", "Smith", "jane.smith@org.com", "912345678",
                "Manager", "jane.shodrone@showdrone.com", TestDataConstants.PASSWORD1);

        registerCustomer(TestDataConstants.CUSTOMER_VAT_2, "Maria", "Silva", "Rua das Flores", "Porto", "4000-123", "Portugal",
                CustomerType.VIP, "Carlos", "Santos", "carlos.santos@empresa.pt", "934567890",
                "CEO", "carlos.shodrone@showdrone.com", TestDataConstants.PASSWORD1);

        registerCustomer(TestDataConstants.CUSTOMER_VAT_3, "Ana", "Costa", "Avenida do Mar", "Faro", "8000-789", "Portugal",
                CustomerType.REGULAR, "Pedro", "Oliveira", "pedro.oliveira@org.com", "965432198",
                "Sales Rep", "pedro.shodrone@showdrone.com", TestDataConstants.PASSWORD1);

        return true;
    }

    private void registerCustomer(final String vat, final String firstName, final String lastName,
                                  final String street, final String city, final String postalCode, final String country,
                                  final CustomerType customerType, final String repFirstName, final String repLastName,
                                  final String repEmail, final String repPhone, final String repPosition,
                                  final String repShodroneEmail, final String repPassword) {
        final RegisterCustomerController controller = new RegisterCustomerController();

        try {
            controller.registerCustomer(vat, firstName, lastName, street, city, postalCode, country,
                    customerType, repFirstName, repLastName, repEmail, repPhone,
                    repPosition, repShodroneEmail, repPassword);
        } catch (final IntegrityViolationException | ConcurrencyException ex) {
            LOGGER.warn("Assuming customer with VAT {} already exists (activate trace log for details)", vat);
            LOGGER.trace("Assuming existing record", ex);
        }
    }
}
