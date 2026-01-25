package lapr4.showproposalmanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.showproposalmanagement.domain.ShowProposal;

public interface ShowProposalRepository extends DomainRepository<Long, ShowProposal> {

    Iterable<ShowProposal> findProposalsAwaitingResponseByRepresentativeOrCustomer(Representative representative);

    Iterable<ShowProposal> findProposalsCustomerAccepted();

    Iterable<ShowProposal> findProposalsAcceptedStateByCustomer(Customer customer);

    Iterable<ShowProposal> findFutureDateAcceptedProposalsByCustomer(Customer customer);
}
