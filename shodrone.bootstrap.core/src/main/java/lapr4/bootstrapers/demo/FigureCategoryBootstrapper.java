package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.figurecategorymanagement.application.AddFigureCategoryController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FigureCategoryBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(FigureCategoryBootstrapper.class);

    @Override
    public boolean execute() {
        registerFigureCategory(TestDataConstants.FIGURE_CATEGORY_NAME_1, "Figures related to architectural designs");
        registerFigureCategory(TestDataConstants.FIGURE_CATEGORY_NAME_2, "Figures related to electronic components");
        registerFigureCategory(TestDataConstants.FIGURE_CATEGORY_NAME_3, "Figures related to mechanical parts");

        return true;
    }

    private void registerFigureCategory(final String name, final String description) {
        final AddFigureCategoryController controller = new AddFigureCategoryController();

        try {
            controller.addFigureCategory(name, description);
        } catch (final IntegrityViolationException | ConcurrencyException ex) {
            LOGGER.warn("Assuming figure category '{}' already exists (activate trace log for details)", name);
            LOGGER.trace("Assuming existing record", ex);
        }
    }
}
