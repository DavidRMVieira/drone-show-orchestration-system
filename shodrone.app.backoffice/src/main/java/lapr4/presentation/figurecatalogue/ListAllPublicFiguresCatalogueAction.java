package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class ListAllPublicFiguresCatalogueAction implements Action {
    @Override
    public boolean execute() {
        return new ListAllPublicFiguresCatalogueUI().show();
    }
}
