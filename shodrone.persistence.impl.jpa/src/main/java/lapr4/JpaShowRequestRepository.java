package lapr4;

import lapr4.customermanagement.domain.Customer;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;

class JpaShowRequestRepository extends ShodroneJpaRepositoryBase<ShowRequest, Long, Long>
        implements ShowRequestRepository {

    public JpaShowRequestRepository() {
        super("id");
    }

    @Override
    public Iterable<ShowRequest> findAllByCustomer(Customer customer) {
        final var query = entityManager().createQuery(
                "SELECT sr FROM ShowRequest sr WHERE sr.customer = :customer",
                ShowRequest.class
        );
        query.setParameter("customer", customer);
        return query.getResultList();
    }

}
