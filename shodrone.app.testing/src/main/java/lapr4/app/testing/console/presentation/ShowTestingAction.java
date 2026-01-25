package lapr4.app.testing.console.presentation;

import eapli.framework.actions.Action;

public class ShowTestingAction implements Action {

    @Override
    public boolean execute() {
        return new ShowTestingUI().show();
    }
}
