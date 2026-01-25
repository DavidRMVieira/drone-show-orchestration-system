package lapr4;

import eapli.framework.identities.impl.NumberSequenceGenerator;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.domain.ShowProposalState;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InMemoryShowProposalRepository extends InMemoryDomainRepository<ShowProposal, Long>
        implements ShowProposalRepository {

    static {
        InMemoryInitializer.init();
    }

    private static final NumberSequenceGenerator gen = new NumberSequenceGenerator();

    public InMemoryShowProposalRepository() {
        super(e -> gen.newId());
    }


    @Override
    public Iterable<ShowProposal> findProposalsAwaitingResponseByRepresentativeOrCustomer(Representative representative) {
        Customer customer = representative.customer();
        List<ShowProposal> result = new ArrayList<>();

        for (ShowProposal proposal : findAll()) {
            boolean isPending = proposal.isAwaitingResponse();
            boolean isForRepresentative = proposal.representative() != null && proposal.representative().equals(representative);
            boolean isForCustomer = proposal.representative() == null && proposal.showRequest().customer().equals(customer);
            if (isPending && (isForRepresentative || isForCustomer)) {
                result.add(proposal);
            }
        }

        return result;
    }

    @Override
    public Iterable<ShowProposal> findProposalsCustomerAccepted() {
        List<ShowProposal> acceptedProposals = new ArrayList<>();
        for (ShowProposal proposal : findAll()) {
            if (proposal.state() == ShowProposalState.CUSTOMER_ACCEPTED) {
                acceptedProposals.add(proposal);
            }
        }
        return acceptedProposals;
    }

    @Override
    public Iterable<ShowProposal> findProposalsAcceptedStateByCustomer(Customer customer) {
        List<ShowProposal> acceptedProposals = new ArrayList<>();
        for (ShowProposal proposal : findAll()) {
            if (proposal.state() == ShowProposalState.ACCEPTED &&
                    proposal.showRequest().customer().equals(customer)) {
                acceptedProposals.add(proposal);
            }
        }
        return acceptedProposals;
    }

    @Override
    public Iterable<ShowProposal> findFutureDateAcceptedProposalsByCustomer(Customer customer) {
        List<ShowProposal> futureAcceptedProposals = new ArrayList<>();
        Date now = new Date();
        for (ShowProposal proposal : findAll()) {
            boolean isAccepted = proposal.state() == ShowProposalState.ACCEPTED;
            boolean belongsToCustomer = proposal.showRequest().customer().equals(customer);
            boolean isFutureDate = proposal.date().after(now);
            if (isAccepted && belongsToCustomer && isFutureDate) {
                futureAcceptedProposals.add(proposal);
            }
        }
        return futureAcceptedProposals;
    }

}
