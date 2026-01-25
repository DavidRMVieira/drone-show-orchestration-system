package lapr4;

import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.repositories.RepresentativeRepository;

import java.util.Optional;

public class InMemoryRepresentativeRepository extends InMemoryDomainRepository<Representative, EmailAddress>
        implements RepresentativeRepository {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public Iterable<Representative> findAllByCustomer(Customer customer) {
        return match(rep -> rep.customer().equals(customer));
    }

    @Override
    public Optional<Representative> findByUser(SystemUser user) {
        for (Representative r : match(r -> r.user().user().equals(user))) {
            return Optional.of(r);
        }
        return Optional.empty();
    }
}
