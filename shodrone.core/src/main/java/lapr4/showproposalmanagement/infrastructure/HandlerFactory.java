package lapr4.showproposalmanagement.infrastructure;

import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.eventHandlers.ShowProposalAcceptedCRMHandler;
import lapr4.showproposalmanagement.eventHandlers.ShowProposalAcceptedHandler;
import lapr4.showproposalmanagement.eventHandlers.ShowProposalRejectedHandler;
import lapr4.showproposalmanagement.eventHandlers.ShowProposalSentHandler;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

public class HandlerFactory {

    private final ShowProposalRepository repo;
    private final ListShowProposalService svc;

    public HandlerFactory(ShowProposalRepository repo, ListShowProposalService svc) {
        this.repo = repo;
        this.svc = svc;
    }

    public ShowProposalAcceptedHandler createAcceptedHandler() {
        return new ShowProposalAcceptedHandler(repo, svc);
    }

    public ShowProposalRejectedHandler createRejectedHandler() {
        return new ShowProposalRejectedHandler(repo, svc);
    }

    public ShowProposalSentHandler createSentHandler() {
        return new ShowProposalSentHandler(svc, repo);
    }

    public ShowProposalAcceptedCRMHandler createAcceptedByCollaboratorHandler() {
        return new ShowProposalAcceptedCRMHandler(repo, svc);
    }
}
