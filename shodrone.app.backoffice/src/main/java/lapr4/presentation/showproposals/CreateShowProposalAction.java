package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class CreateShowProposalAction implements Action {

    @Override
    public boolean execute() {
        return new CreateShowProposalUI().show();
    }
}
