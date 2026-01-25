package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.pubsub.EventPublisher;
import eapli.framework.infrastructure.pubsub.impl.inprocess.service.InProcessPubSub;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class AcceptProposalByCollaboratorController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ListShowProposalService svc = new ListShowProposalService();
    private final EventPublisher publisher = InProcessPubSub.publisher();

    public Iterable<ShowProposalDTO> checkPendingProposalAcceptedByCustomer() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_COLLABORATOR);

        return svc.allProposalsCustomerAccepted();
    }

    public void acceptProposalByCollaborator(ShowProposalDTO showProposalDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal proposal = svc.findShowProposalByID(showProposalDTO.id());

        if (!proposal.isAcceptedByCustomer()) {
            throw new IllegalStateException("Proposal is not in a state to be accepted by a collaborator.");
        }

        publisher.publish(new ShowProposalAcceptedByCRMEvent(proposal.identity(), proposal.showRequest().customer(), authz.session().get().authenticatedUser()));
    }
}