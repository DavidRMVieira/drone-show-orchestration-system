package lapr4.presentation.figurecategories;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.figurecategorymanagement.application.InactivateFigureCategoryController;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

@SuppressWarnings("squid:S106")
public class InactivateFigureCategoryUI extends AbstractUI {

    private final InactivateFigureCategoryController controller = new InactivateFigureCategoryController();

    @Override
    protected boolean doShow() {
        final Iterable<FigureCategoryDTO> activeCategories = controller.activeFigureCategories();

        if (!activeCategories.iterator().hasNext()) {
            System.out.println("There are no active categories");
        } else {
            FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
            final SelectWidget<FigureCategoryDTO> selector = new SelectWidget<>(printer.header(), activeCategories, printer);
            selector.show();
            final FigureCategoryDTO selectedCategory = selector.selectedElement();

            if (selectedCategory != null) {
                try {
                    FigureCategoryDTO category = controller.inactivateFigureCategory(selectedCategory);
                    showCategoryResult(category);
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out.println(
                            "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }

        return false;
    }

    private void showCategoryResult(FigureCategoryDTO figureCategory) {
        System.out.println("\n=== Changes successful ===");

        FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
        System.out.println(printer.header());
        printer.visit(figureCategory);
    }

    @Override
    public String headline() {
        return "Inactivate Figure Category";
    }
}

