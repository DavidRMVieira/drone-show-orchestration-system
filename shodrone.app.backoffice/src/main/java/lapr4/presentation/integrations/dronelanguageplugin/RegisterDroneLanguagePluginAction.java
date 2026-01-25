package lapr4.presentation.integrations.dronelanguageplugin;

import eapli.framework.actions.Action;

public class RegisterDroneLanguagePluginAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterDroneLanguagePluginUI().show();
    }
}
