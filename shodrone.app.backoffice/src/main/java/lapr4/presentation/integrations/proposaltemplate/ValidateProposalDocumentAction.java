package lapr4.presentation.integrations.proposaltemplate;

import eapli.framework.actions.Action;

public class ValidateProposalDocumentAction implements Action {

    @Override
    public boolean execute() {
        return new ValidateProposalDocumentUI().show();
    }
}
