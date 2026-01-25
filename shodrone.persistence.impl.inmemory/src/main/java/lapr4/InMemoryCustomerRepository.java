package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.VAT;
import lapr4.customermanagement.repositories.CustomerRepository;

public class InMemoryCustomerRepository extends InMemoryDomainRepository<Customer, VAT>
        implements CustomerRepository {

    static {
        InMemoryInitializer.init();
    }

}
