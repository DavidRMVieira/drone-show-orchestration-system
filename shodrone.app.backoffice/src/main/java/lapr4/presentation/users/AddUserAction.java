package lapr4.presentation.users;

import eapli.framework.actions.Action;

public class AddUserAction implements Action {

    @Override
    public boolean execute() {
        return new AddUserUI().show();
    }
}
