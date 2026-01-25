package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

public class InMemoryShodroneUserRepository extends InMemoryDomainRepository<ShodroneUser, ShodroneEmail>
        implements ShodroneUserRepository {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public Iterable<ShodroneUser> findAllActive() {
        return match(e -> e.user().isActive());
    }

    @Override
    public Iterable<ShodroneUser> findAllDisabled() {
        return match(e -> !e.user().isActive());
    }

}
