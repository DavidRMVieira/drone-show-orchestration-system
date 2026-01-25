package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.pubsub.EventPublisher;
import eapli.framework.infrastructure.pubsub.impl.inprocess.service.InProcessPubSub;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class EvaluateProposalController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final RepresentativeRepository representativeRepository = PersistenceContext.repositories().representatives();
    private final ListShowProposalService svc = new ListShowProposalService();
    private final EventPublisher publisher = InProcessPubSub.publisher();


    public boolean acceptProposal(Long showProposalID) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.REPRESENTATIVE);

        SystemUser currentUser = authz.session().get().authenticatedUser();
        Representative representative = representativeRepository.findByUser(currentUser).get();

        ShowProposal proposal = svc.findShowProposalByID(showProposalID);

        if (!proposal.isAwaitingResponse()) {
            return false;
        }

        publisher.publish(new ShowProposalAcceptedEvent(proposal.identity(), proposal.showRequest().customer(), representative));

        return true;
    }

    public boolean rejectProposal(Long showProposalID, String feedback) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.REPRESENTATIVE);

        SystemUser currentUser = authz.session().get().authenticatedUser();
        Representative representative = representativeRepository.findByUser(currentUser).get();

        ShowProposal proposal = svc.findShowProposalByID(showProposalID);

        if (!proposal.isAwaitingResponse()) {
            return false;
        }

        publisher.publish(new ShowProposalRejectedEvent(proposal.identity(), proposal.showRequest().customer(), representative, feedback));

        return true;
    }

}
