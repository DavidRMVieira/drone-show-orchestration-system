package lapr4.app.customer.console.presentation;

import eapli.framework.actions.Action;

public class EvaluateProposalAction implements Action {
    @Override
    public boolean execute() {
        return new EvaluateProposalUI().show();
    }
}
