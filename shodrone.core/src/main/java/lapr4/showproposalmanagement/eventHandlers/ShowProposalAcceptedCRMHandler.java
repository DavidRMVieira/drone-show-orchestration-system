package lapr4.showproposalmanagement.eventHandlers;

import eapli.framework.domain.events.DomainEvent;
import eapli.framework.infrastructure.pubsub.EventHandler;
import eapli.framework.validations.Preconditions;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

public class ShowProposalAcceptedCRMHandler implements EventHandler {

    private final ShowProposalRepository repo;
    private final ListShowProposalService svc;

    public ShowProposalAcceptedCRMHandler(ShowProposalRepository repo, ListShowProposalService svc) {
        this.repo = repo;
        this.svc = svc;
    }

    @Override
    public void onEvent(DomainEvent event) {
        Preconditions.ensure(event instanceof ShowProposalAcceptedByCRMEvent);

        ShowProposalAcceptedByCRMEvent sentEvent = (ShowProposalAcceptedByCRMEvent) event;

        final ShowProposal proposal = svc.findShowProposalByID(sentEvent.proposalID());
        proposal.acceptedByCollaborator(sentEvent);
        repo.save(proposal);

    }
}