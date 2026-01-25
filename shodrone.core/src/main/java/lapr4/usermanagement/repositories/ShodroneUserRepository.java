package lapr4.usermanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneUser;

public interface ShodroneUserRepository extends DomainRepository<ShodroneEmail, ShodroneUser> {

    Iterable<ShodroneUser> findAllActive();

    Iterable<ShodroneUser> findAllDisabled();

}
