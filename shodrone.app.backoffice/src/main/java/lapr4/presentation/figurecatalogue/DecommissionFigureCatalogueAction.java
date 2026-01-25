package lapr4.presentation.figurecatalogue;

import eapli.framework.actions.Action;

public class DecommissionFigureCatalogueAction implements Action {
    @Override
    public boolean execute() {
        return new DecommissionFigureCatalogueUI().doShow();
    }
}
