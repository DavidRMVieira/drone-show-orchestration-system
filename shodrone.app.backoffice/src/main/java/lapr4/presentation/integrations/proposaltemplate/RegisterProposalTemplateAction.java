package lapr4.presentation.integrations.proposaltemplate;

import eapli.framework.actions.Action;

public class RegisterProposalTemplateAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterProposalTemplateUI().show();
    }
}
