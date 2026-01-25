package lapr4.presentation.integrations.dslplugin;

import eapli.framework.actions.Action;

public class ValidateDSLDescriptionAction implements Action {

    @Override
    public boolean execute() {
        return new ValidateDSLDescriptionUI().show();
    }

}