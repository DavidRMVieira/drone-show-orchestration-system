package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class SendProposalAction implements Action {
    @Override
    public boolean execute() {
        return new SendProposalUI().show();
    }
}
