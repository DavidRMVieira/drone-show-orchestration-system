package lapr4.customermanagement.application;

import eapli.framework.application.ApplicationService;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.VAT;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.customermanagement.repositories.CustomerRepository;
import lapr4.infrastructure.persistence.PersistenceContext;

@ApplicationService
public class ListCustomerService {

    private final CustomerRepository customerRepository = PersistenceContext.repositories().customers();

    public Iterable<CustomerDTO> allCustomers() {
        final Iterable<Customer> customers = customerRepository.findAll();
        return CustomerDTOParser.transformToDTO(customers);
    }

    public Customer findCustomerByVatNumber(final String vatNumber) {
        return customerRepository.ofIdentity(VAT.valueOf(vatNumber))
                .orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + vatNumber));
    }

}
