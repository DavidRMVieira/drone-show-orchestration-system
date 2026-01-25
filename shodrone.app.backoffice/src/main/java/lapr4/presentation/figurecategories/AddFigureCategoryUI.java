package lapr4.presentation.figurecategories;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.figurecategorymanagement.application.AddFigureCategoryController;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

@SuppressWarnings("java:S106")
public class AddFigureCategoryUI extends AbstractUI {

    private final AddFigureCategoryController controller = new AddFigureCategoryController();

    @Override
    protected boolean doShow() {

        final String name = Console.readLine("Name: ");
        final String description = Console.readLine("Description: ");

        try {
            FigureCategoryDTO figureCategory = controller.addFigureCategory(name, description);
            showRegistrationResult(figureCategory);
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That name is already in use.");
        }

        return false;
    }

    private void showRegistrationResult(FigureCategoryDTO figureCategory) {
        System.out.println("\n=== Registration Successful ===");

        FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
        System.out.println(printer.header());
        printer.visit(figureCategory);
    }

    @Override
    public String headline() {
        return "Add Figure Category";
    }

}