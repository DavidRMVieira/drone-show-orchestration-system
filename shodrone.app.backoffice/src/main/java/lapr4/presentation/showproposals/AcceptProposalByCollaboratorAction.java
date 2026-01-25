package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class AcceptProposalByCollaboratorAction implements Action {
    @Override
    public boolean execute() {
        return new AcceptProposalByCollaboratorUI().doShow();
    }
}