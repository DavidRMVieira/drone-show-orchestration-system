package lapr4.showproposalmanagement.eventHandlers;

import eapli.framework.domain.events.DomainEvent;
import eapli.framework.infrastructure.pubsub.EventHandler;
import eapli.framework.validations.Preconditions;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

public class ShowProposalRejectedHandler implements EventHandler {

    private final ShowProposalRepository showProposalRepository;
    private final ListShowProposalService svc;

    public ShowProposalRejectedHandler(ShowProposalRepository showProposalRepository, ListShowProposalService svc) {
        this.showProposalRepository = showProposalRepository;
        this.svc = svc;
    }

    @Override
    public void onEvent(DomainEvent event) {
        Preconditions.ensure(event instanceof ShowProposalRejectedEvent);

        ShowProposalRejectedEvent sentEvent = (ShowProposalRejectedEvent) event;

        final ShowProposal proposal = svc.findShowProposalByID(sentEvent.proposalID());
        proposal.rejectedByCustomer(sentEvent);
        showProposalRepository.save(proposal);
    }
}
