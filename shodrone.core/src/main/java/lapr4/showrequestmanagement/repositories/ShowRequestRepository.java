package lapr4.showrequestmanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.showrequestmanagement.domain.ShowRequest;

public interface ShowRequestRepository extends DomainRepository<Long, ShowRequest> {

    Iterable<ShowRequest> findAllByCustomer(Customer customer);

}
