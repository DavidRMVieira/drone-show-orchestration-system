package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class SearchFigureByCategoryAction implements Action {
    @Override
    public boolean execute() {
        return new SearchFigureByCategoryUI().show();
    }
}
