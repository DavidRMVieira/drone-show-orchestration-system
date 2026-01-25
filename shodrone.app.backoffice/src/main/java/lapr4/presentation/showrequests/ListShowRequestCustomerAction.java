package lapr4.presentation.showrequests;

import eapli.framework.actions.Action;

public class ListShowRequestCustomerAction implements Action {

    @Override
    public boolean execute() {
        return new ListShowRequestCustomerUI().show();
    }
}
