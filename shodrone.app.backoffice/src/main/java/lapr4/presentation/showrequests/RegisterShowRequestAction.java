package lapr4.presentation.showrequests;

import eapli.framework.actions.Action;

public class RegisterShowRequestAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterShowRequestUI().show();
    }
}
