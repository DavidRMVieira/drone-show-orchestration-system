package lapr4.presentation.figurecategories;

import eapli.framework.actions.Action;

public class ListFigureCategoryAction implements Action {

    @Override
    public boolean execute() {
        return new ListFigureCategoriesUI().show();
    }

}