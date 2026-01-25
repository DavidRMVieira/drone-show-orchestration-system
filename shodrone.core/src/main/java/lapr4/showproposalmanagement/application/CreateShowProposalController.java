package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.customermanagement.application.RepresentativeDTOParser;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.dto.RepresentativeDTO;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.domain.ShowProposalBuilder;
import lapr4.showproposalmanagement.domain.ShowProposalState;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.showrequestmanagement.application.ShowRequestDTOParser;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.Date;

@UseCaseController
public class CreateShowProposalController {

    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;
    private final RepresentativeRepository representativeRepository;
    private final ShowProposalRepository showProposalRepository;

    public CreateShowProposalController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequests();
        this.representativeRepository = PersistenceContext.repositories().representatives();
        this.showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public ShowProposalDTO createShowProposal(ShowProposalDTO proposalDTO, ShowRequestDTO selectedRequestDTO, RepresentativeDTO selectedRepresentativeDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        final ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(selectedRequestDTO);

        ShowProposalBuilder showProposalBuilder = new ShowProposalBuilder().withDate(proposalDTO.date()).withDuration(proposalDTO.duration()).withState(ShowProposalState.IN_CONSTRUCTION).withLocation(proposalDTO.latitudeLocation(), proposalDTO.longitudeLocation())
                .withNumberOfDrones(proposalDTO.numberOfDrones()).withInsuranceAmount(proposalDTO.insuranceAmount()).withShowRequest(showRequest);

        if (selectedRepresentativeDTO != null) {
            final Representative representative = new RepresentativeDTOParser(representativeRepository).valueOf(selectedRepresentativeDTO);
            showProposalBuilder.withRepresentative(representative);
        }

        ShowProposal showProposal = showProposalBuilder.build();

        return showProposalRepository.save(showProposal).toDTO();
    }

    public Iterable<ShowRequestDTO> listShowRequests() {
        final Iterable<ShowRequest> showRequests = showRequestRepository.findAll();
        return ShowRequestDTOParser.transformToDTO(showRequests);
    }

    public Iterable<RepresentativeDTO> listCustomerRepresentatives(ShowRequestDTO selectedRequestDTO) {
        final ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(selectedRequestDTO);
        final Customer customer = showRequest.customer();

        final Iterable<Representative> representatives = representativeRepository.findAllByCustomer(customer);
        return RepresentativeDTOParser.transformToDTO(representatives);
    }

}
