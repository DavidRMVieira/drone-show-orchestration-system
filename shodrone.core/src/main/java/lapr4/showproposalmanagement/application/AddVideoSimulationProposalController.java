package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.domain.VideoFile;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class AddVideoSimulationProposalController {

    private final AuthorizationService authz;
    private final ListShowProposalService proposalSvc;
    private final ShowProposalRepository showProposalRepository;

    public AddVideoSimulationProposalController() {
        this.authz = AuthzRegistry.authorizationService();
        this.proposalSvc = new ListShowProposalService();
        this.showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public void addVideoSimulationProposal(final ShowProposalDTO selectedProposalDTO, String videoLink) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedProposalDTO.id());

        showProposal.addVideoSimulation(VideoFile.valueOf(videoLink));

        showProposalRepository.save(showProposal);
    }

    public Iterable<ShowProposalDTO> listShowProposals() {
        return proposalSvc.allShowProposals();
    }

}

