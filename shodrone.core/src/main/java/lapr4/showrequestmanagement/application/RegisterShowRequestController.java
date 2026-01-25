package lapr4.showrequestmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.customermanagement.application.ListCustomerService;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.Place;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.domain.ShowRequestState;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.Date;

@UseCaseController
public class RegisterShowRequestController {

    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;
    private final ListCustomerService svcCustomers;

    public RegisterShowRequestController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequests();
        this.svcCustomers = new ListCustomerService();
    }

    public ShowRequestDTO registerShowRequest(String place, Date date, int duration, CustomerDTO customerDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        final Customer customer = svcCustomers.findCustomerByVatNumber(customerDTO.getVatNumber());
        ShowRequest showRequest = new ShowRequest(Place.valueOf(place), date, Duration.valueOf(duration),ShowRequestState.PENDING, customer);
        return showRequestRepository.save(showRequest).toDTO();
    }

    public Iterable<CustomerDTO> listCustomer() {
        return svcCustomers.allCustomers();
    }

}
