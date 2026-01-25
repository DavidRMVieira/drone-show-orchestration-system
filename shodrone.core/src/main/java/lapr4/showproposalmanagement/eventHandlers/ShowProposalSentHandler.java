package lapr4.showproposalmanagement.eventHandlers;

import eapli.framework.domain.events.DomainEvent;
import eapli.framework.infrastructure.pubsub.EventHandler;
import eapli.framework.validations.Preconditions;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

public class ShowProposalSentHandler implements EventHandler {

    private final ShowProposalRepository repo;
    private final ListShowProposalService svc;

    public ShowProposalSentHandler(ListShowProposalService svc, ShowProposalRepository repo) {
        this.svc = svc;
        this.repo = repo;
    }

    @Override
    public void onEvent(DomainEvent event) {
        Preconditions.ensure(event instanceof ShowProposalSentEvent);

        ShowProposalSentEvent sentEvent = (ShowProposalSentEvent) event;

        final ShowProposal proposal = svc.findShowProposalByID(sentEvent.proposalID());

        proposal.sent(sentEvent);

        repo.save(proposal);
    }
}
