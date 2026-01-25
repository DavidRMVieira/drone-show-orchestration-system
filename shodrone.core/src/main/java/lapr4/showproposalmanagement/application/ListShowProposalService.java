package lapr4.showproposalmanagement.application;

import eapli.framework.application.ApplicationService;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

import java.util.Optional;

@ApplicationService
public class ListShowProposalService {

    private final ShowProposalRepository repository = PersistenceContext.repositories().showProposals();

    public Iterable<ShowProposalDTO> allShowProposals() {
        final Iterable<ShowProposal> proposals = repository.findAll();
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

    public ShowProposal findShowProposalByID(final Long id) {
        return repository.ofIdentity(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown show proposal: " + id));
    }


    public Iterable<ShowProposalDTO> allProposalsAwaitingResponseByRepresentativeOrCustomer(Representative representative) {
        final Iterable<ShowProposal> proposals = repository.findProposalsAwaitingResponseByRepresentativeOrCustomer(representative);
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

    public Iterable<ShowProposalDTO> allProposalsCustomerAccepted() {
        final Iterable<ShowProposal> proposals = repository.findProposalsCustomerAccepted();
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

    public Iterable<ShowProposalDTO> allShowsCRMCollaboratorAccepted() {
        final Iterable<ShowProposal> proposals = repository.findShowsCRMCollaboratorAccepted();
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

    public Iterable<ShowProposalDTO> allShowsByCustomer(Customer customer) {
        final Iterable<ShowProposal> proposals = repository.findProposalsAcceptedStateByCustomer(customer);
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

    public Iterable<ShowProposalDTO> allScheduledShowsByCustomer(Customer customer) {
        final Iterable<ShowProposal> proposals = repository.findFutureDateAcceptedProposalsByCustomer(customer);
        return ShowProposalDTOParser.transformToDTO(proposals);
    }

}
