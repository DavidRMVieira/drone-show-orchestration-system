package lapr4.presentation.figurecategories;

import eapli.framework.actions.Action;

public class ChangeFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new ChangeFigureCategoryUI().show();
    }

}