package lapr4;

import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.domain.ShowProposalState;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;


class JpaShowProposalRepository extends ShodroneJpaRepositoryBase<ShowProposal, Long, Long>
        implements ShowProposalRepository {

    public JpaShowProposalRepository() {
        super("id");
    }


    @Override
    public Iterable<ShowProposal> findProposalsAwaitingResponseByRepresentativeOrCustomer(Representative representative) {
        return entityManager()
                .createQuery("""
                SELECT p FROM ShowProposal p
                LEFT JOIN p.drones d
                LEFT JOIN p.figures f
                WHERE p.state = :state
                  AND (
                    (p.representative = :rep)
                    OR (p.representative IS NULL AND p.showRequest.customer = :cust)
                  )
            """, ShowProposal.class)
                .setParameter("rep", representative)
                .setParameter("cust", representative.customer())
                .setParameter("state", ShowProposalState.AWAITING_RESPONSE)
                .getResultList();
    }

    @Override
    public Iterable<ShowProposal> findProposalsCustomerAccepted() {
        return entityManager()
                .createQuery("SELECT p FROM ShowProposal p WHERE p.state = :state", ShowProposal.class)
                .setParameter("state", ShowProposalState.CUSTOMER_ACCEPTED)
                .getResultList();
    }

    @Override
    public Iterable<ShowProposal> findProposalsAcceptedStateByCustomer(Customer customer) {
        return entityManager()
                .createQuery("""
                SELECT p FROM ShowProposal p
                LEFT JOIN p.drones d
                LEFT JOIN p.figures f
                WHERE p.state = :state
                  AND p.showRequest.customer = :customer
            """, ShowProposal.class)
                .setParameter("state", ShowProposalState.ACCEPTED)
                .setParameter("customer", customer)
                .getResultList();
    }

    @Override
    public Iterable<ShowProposal> findFutureDateAcceptedProposalsByCustomer(Customer customer) {
        return entityManager()
                .createQuery("""
                SELECT p FROM ShowProposal p
                LEFT JOIN p.drones d
                LEFT JOIN p.figures f
                WHERE p.state = :state
                  AND p.showRequest.customer = :customer
                  AND p.date > CURRENT_DATE
            """, ShowProposal.class)
                .setParameter("state", ShowProposalState.ACCEPTED)
                .setParameter("customer", customer)
                .getResultList();
    }
}
