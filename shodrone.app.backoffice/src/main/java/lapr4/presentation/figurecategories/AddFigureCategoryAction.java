package lapr4.presentation.figurecategories;

import eapli.framework.actions.Action;

public class AddFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new AddFigureCategoryUI().show();
    }

}