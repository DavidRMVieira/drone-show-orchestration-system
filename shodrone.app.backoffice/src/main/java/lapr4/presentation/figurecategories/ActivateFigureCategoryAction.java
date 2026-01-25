package lapr4.presentation.figurecategories;

import eapli.framework.actions.Action;

public class ActivateFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new ActivateFigureCategoryUI().show();
    }
}
