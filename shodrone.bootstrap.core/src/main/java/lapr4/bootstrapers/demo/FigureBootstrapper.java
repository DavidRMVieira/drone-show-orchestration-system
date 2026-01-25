package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.figurecatalogue.application.AddFigureCatalogueController;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

public class FigureBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(FigureBootstrapper.class);

    @Override
    public boolean execute() {
        Set<String> keywords1 = new HashSet<>();
        keywords1.add("architecture");
        keywords1.add("blueprint");

        Set<String> keywords2 = new HashSet<>();
        keywords2.add("engineering");
        keywords2.add("draft");

        Set<String> keywords3 = new HashSet<>();
        keywords3.add("design");
        keywords3.add("structure");

        registerFigure("Modern House Blueprint", "DSL for House", "1.0", "FIG001", "Static", "1.0",
                new CustomerDTO(TestDataConstants.CUSTOMER_VAT_1, null, null, null, null), new FigureCategoryDTO(TestDataConstants.FIGURE_CATEGORY_NAME_1, null, true), keywords1);

        registerFigure("Bridge Structure", "DSL for Bridge", "2.0", "FIG002", "Static", "1.1",
                new CustomerDTO(TestDataConstants.CUSTOMER_VAT_2, null, null, null, null), new FigureCategoryDTO(TestDataConstants.FIGURE_CATEGORY_NAME_2, null, true), keywords2);

        registerFigure("Futuristic Tower", "DSL for Tower", "1.2", "FIG003", "Dynamic", "2.0",
                new CustomerDTO(TestDataConstants.CUSTOMER_VAT_3, null, null, null, null), new FigureCategoryDTO(TestDataConstants.FIGURE_CATEGORY_NAME_3, null, true), keywords3);

        return true;
    }

    private void registerFigure(final String description, final String dslDescription, final String dslVersion,
                                final String code, final String figureType, final String figureVersion,
                                final CustomerDTO customerDTO, final FigureCategoryDTO categoryDTO, final Set<String> keywords) {
        final AddFigureCatalogueController controller = new AddFigureCatalogueController();

        try {
            controller.addFigureToCatalogue(description, dslDescription, dslVersion, code,
                    figureType, figureVersion, customerDTO, categoryDTO, keywords, false);
        } catch (final IntegrityViolationException | ConcurrencyException ex) {
            LOGGER.warn("Assuming figure with code {} already exists (activate trace log for details)", code);
            LOGGER.trace("Assuming existing record", ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}