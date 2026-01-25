package lapr4.showrequestmanagement.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.customermanagement.application.ListCustomerService;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

public class ListShowRequestCustomerController {

    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;
    private final ListCustomerService svcCustomers;

    public ListShowRequestCustomerController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequests();
        this.svcCustomers = new ListCustomerService();
    }

    public Iterable<ShowRequestDTO> listShowRequestCustomer(CustomerDTO customerDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);

        final Customer customer = svcCustomers.findCustomerByVatNumber(customerDTO.getVatNumber());
        final Iterable<ShowRequest> showRequests = showRequestRepository.findAllByCustomer(customer);
        return ShowRequestDTOParser.transformToDTO(showRequests);
    }

    public Iterable<CustomerDTO> listCustomer() {
        return svcCustomers.allCustomers();
    }
}
