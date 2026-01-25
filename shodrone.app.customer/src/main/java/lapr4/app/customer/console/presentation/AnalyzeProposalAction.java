package lapr4.app.customer.console.presentation;

import eapli.framework.actions.Action;

public class AnalyzeProposalAction implements Action {

    @Override
    public boolean execute() {
        return new AnalyzeProposalUI().show();
    }
}
