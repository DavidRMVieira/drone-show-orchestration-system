package lapr4.presentation.figurecategories;

import eapli.framework.actions.Action;

public class InactivateFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new InactivateFigureCategoryUI().show();
    }

}
