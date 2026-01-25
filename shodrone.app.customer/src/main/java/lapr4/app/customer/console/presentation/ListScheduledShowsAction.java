package lapr4.app.customer.console.presentation;

import eapli.framework.actions.Action;

public class ListScheduledShowsAction implements Action {

    @Override
    public boolean execute() {
        return new ListScheduledShowsUI().show();
    }
}
