package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.pubsub.EventPublisher;
import eapli.framework.infrastructure.pubsub.impl.inprocess.service.InProcessPubSub;
import lapr4.integrations.proposaltemplate.application.GenerateProposalDocumentService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.io.IOException;

@UseCaseController
public class SendProposalController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ListShowProposalService proposalSvc = new ListShowProposalService();
    private final EventPublisher publisher = InProcessPubSub.publisher();
    private final GenerateProposalDocumentService documentSvc = new GenerateProposalDocumentService();

    public void sendProposal(ShowProposalDTO showProposalDTO, String language, String proposalTemplateVersion) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal proposal = proposalSvc.findShowProposalByID(showProposalDTO.id());

        if (!proposal.isReadyToSend()) {
            throw new IllegalArgumentException("Proposal is not in a state to be sent.");
        }

        String document = documentSvc.proposalDocument(proposal, language, proposalTemplateVersion);

        SystemUser crmCollaborator = authz.session().get().authenticatedUser();

        ShowProposalSentEvent event;
        if (proposal.hasRepresentative()) {
            event = new ShowProposalSentEvent(proposal.identity(), crmCollaborator, proposal.representative(), document);
        } else {
            event = new ShowProposalSentEvent(proposal.identity(), crmCollaborator, proposal.showRequest().customer(), document);
        }

        publisher.publish(event);
    }

    public Iterable<ShowProposalDTO> allShowProposals() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);
        return proposalSvc.allShowProposals();
    }

}
