package lapr4.presentation.customers;

import eapli.framework.actions.Action;

public class RegisterCustomerAction implements Action {

    @Override
    public boolean execute() {
        return new RegisterCustomerUI().show();
    }

}
