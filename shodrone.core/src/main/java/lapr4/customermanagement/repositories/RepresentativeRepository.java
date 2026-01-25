package lapr4.customermanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;

import java.util.Optional;

public interface RepresentativeRepository extends DomainRepository<EmailAddress, Representative> {

    Iterable<Representative> findAllByCustomer(Customer customer);

    Optional<Representative> findByUser(SystemUser user);
}
