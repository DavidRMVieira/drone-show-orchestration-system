package lapr4.customermanagement.application;

import eapli.framework.application.UseCaseController;
import lapr4.customermanagement.domain.CustomerType;
import lapr4.customermanagement.dto.CustomerDTO;

@UseCaseController
public class RegisterCustomerController {

    private final RegisterCustomerService svc = new RegisterCustomerService();

    public CustomerDTO registerCustomer(final String customerVatNumber, final String customerFirstName, final String customerLastName,
                                        final String street, final String city, final String postalCode, final String country,
                                        final CustomerType customerType, final String representativeFirstName,
                                        final String representativeLastName, final String representativeEmail,
                                        final String representativePhoneNumber, final String representativePosition,
                                        final String representativeShodroneEmail, final String representativePassword) {

        return svc.registerCustomer(customerVatNumber, customerFirstName, customerLastName, street, city, postalCode, country,
                customerType, representativeFirstName, representativeLastName, representativeEmail, representativePhoneNumber,
                representativePosition, representativeShodroneEmail, representativePassword);
    }

}
