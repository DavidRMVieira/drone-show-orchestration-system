package lapr4.presentation.showproposals;

import eapli.framework.actions.Action;

public class AddFigureProposalAction implements Action {
    @Override
    public boolean execute() {
        return new AddFigureProposalUI().show();
    }
}
