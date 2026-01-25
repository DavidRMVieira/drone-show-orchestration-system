package lapr4.showproposalmanagement.eventHandlers;

import eapli.framework.domain.events.DomainEvent;
import eapli.framework.infrastructure.pubsub.EventHandler;
import eapli.framework.validations.Preconditions;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

public class ShowProposalAcceptedHandler implements EventHandler {

    private final ShowProposalRepository showProposalRepository;
    private final ListShowProposalService svc;

    public ShowProposalAcceptedHandler(ShowProposalRepository showProposalRepository, ListShowProposalService svc) {
        this.showProposalRepository = showProposalRepository;
        this.svc = svc;
    }

    @Override
    public void onEvent(DomainEvent event) {
        Preconditions.ensure(event instanceof ShowProposalAcceptedEvent);

        ShowProposalAcceptedEvent sentEvent = (ShowProposalAcceptedEvent) event;

        final ShowProposal proposal = svc.findShowProposalByID(sentEvent.proposalID());
        proposal.acceptedByCustomer(sentEvent);
        showProposalRepository.save(proposal);

    }
}
