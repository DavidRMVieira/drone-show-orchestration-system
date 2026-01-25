package lapr4.presentation.users;

import eapli.framework.actions.Action;

public class EnableUserAction implements Action {

    @Override
    public boolean execute() {
        return new EnableUserUI().show();
    }
}
