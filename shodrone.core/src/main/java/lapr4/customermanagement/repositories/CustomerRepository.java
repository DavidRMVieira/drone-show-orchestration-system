package lapr4.customermanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.VAT;

import java.util.Optional;

public interface CustomerRepository extends DomainRepository<VAT, Customer> {

}
