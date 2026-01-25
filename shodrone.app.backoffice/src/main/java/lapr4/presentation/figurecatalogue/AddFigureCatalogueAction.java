package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class AddFigureCatalogueAction implements Action {
    @Override
    public boolean execute() {
        return new AddFigureCatalogueUI().show();
    }
}
