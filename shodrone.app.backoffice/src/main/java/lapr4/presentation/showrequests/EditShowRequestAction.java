package lapr4.presentation.showrequests;

import eapli.framework.actions.Action;

public class EditShowRequestAction implements Action {

    @Override
    public boolean execute() {
        return new EditShowRequestUI().show();
    }
}

