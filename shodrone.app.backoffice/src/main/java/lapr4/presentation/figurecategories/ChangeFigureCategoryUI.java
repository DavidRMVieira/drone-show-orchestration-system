package lapr4.presentation.figurecategories;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.figurecategorymanagement.application.ChangeFigureCategoryController;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

public class ChangeFigureCategoryUI extends AbstractUI {

    private final ChangeFigureCategoryController controller = new ChangeFigureCategoryController();

    @Override
    protected boolean doShow() {
        final Iterable<FigureCategoryDTO> figureCategories = controller.allFigureCategories();

        if (!figureCategories.iterator().hasNext()) {
            System.out.println("There are no categories");
        } else {
            FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
            final SelectWidget<FigureCategoryDTO> selector = new SelectWidget<>(printer.header(), figureCategories, printer);
            selector.show();
            FigureCategoryDTO selectedCategory = selector.selectedElement();

            if (selectedCategory != null) {
                try {
                    System.out.println("Name: " + selectedCategory.getName());
                    boolean changeName = Console.readBoolean("Do you want to change the name? (y/n)");
                    if (changeName) {
                        String newName = Console.readLine("New Name: ");
                        selectedCategory = controller.changeFigureCategoryName(newName, selectedCategory);
                    }
                    System.out.println("Description: " + selectedCategory.getDescription());
                    boolean changeDescription = Console.readBoolean("Do you want to change the description? (y/n)");
                    if (changeDescription) {
                        String newDescription = Console.readLine("New Description: ");
                        selectedCategory = controller.changeFigureCategoryDescription(newDescription, selectedCategory);
                    }

                    showRegistrationResult(selectedCategory);
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out.println(
                            "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }

        return false;
    }

    private void showRegistrationResult(FigureCategoryDTO figureCategory) {
        System.out.println("\n=== Changes successful ===");

        FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
        System.out.println(printer.header());
        printer.visit(figureCategory);
    }

    @Override
    public String headline() {
        return "Change a Figure Category";
    }

}