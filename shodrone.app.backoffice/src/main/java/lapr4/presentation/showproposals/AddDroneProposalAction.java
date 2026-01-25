package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class AddDroneProposalAction implements Action {

    @Override
    public boolean execute() {
        return new AddDroneProposalUI().show();
    }
}
