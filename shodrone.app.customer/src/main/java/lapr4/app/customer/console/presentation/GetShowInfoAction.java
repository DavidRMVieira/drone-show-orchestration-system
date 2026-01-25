package lapr4.app.customer.console.presentation;

import eapli.framework.actions.Action;

public class GetShowInfoAction implements Action {
    @Override
    public boolean execute() {
        return new GetShowInfoUI().show();
    }
}
