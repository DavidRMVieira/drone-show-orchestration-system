package lapr4.presentation.integrations.dslplugin;

import eapli.framework.actions.Action;
import lapr4.presentation.integrations.dronelanguageplugin.RegisterDroneLanguagePluginUI;

public class RegisterDSLPluginAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterDSLPluginUI().show();
    }
}
