package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class SearchFigureByKeywordAction implements Action {
    @Override
    public boolean execute() {
        return new SearchFigureByKeywordUI().show();
    }
}
