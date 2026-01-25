package lapr4.presentation.integrations.dronelanguageplugin;

import eapli.framework.actions.Action;

public class ValidateDroneLanguageAction implements Action {

    @Override
    public boolean execute() {
        return new ValidateDroneLanguageUI().show();
    }
}
