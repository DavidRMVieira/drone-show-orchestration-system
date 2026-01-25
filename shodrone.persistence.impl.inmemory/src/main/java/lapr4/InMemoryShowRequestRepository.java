package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import eapli.framework.identities.impl.NumberSequenceGenerator;
import lapr4.customermanagement.domain.Customer;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;

public class InMemoryShowRequestRepository extends InMemoryDomainRepository<ShowRequest, Long>
        implements ShowRequestRepository {

    static {
        InMemoryInitializer.init();
    }

    private static final NumberSequenceGenerator gen = new NumberSequenceGenerator();

    public InMemoryShowRequestRepository() {
        super(e -> gen.newId());
    }

    @Override
    public Iterable<ShowRequest> findAllByCustomer(Customer customer) {
        return match(sr -> sr.customer().equals(customer));
    }
}
