package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class SearchFigureByCategoryAndKeywordAction implements Action {
    @Override
    public boolean execute() {
        return new SearchFigureByCategoryAndKeywordUI().show();
    }
}
