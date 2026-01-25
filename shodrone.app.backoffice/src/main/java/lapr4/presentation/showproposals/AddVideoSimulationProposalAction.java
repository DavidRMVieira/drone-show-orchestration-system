package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class AddVideoSimulationProposalAction implements Action {

    @Override
    public boolean execute() {
        return new AddVideoSimulationProposalUI().show();
    }
}
