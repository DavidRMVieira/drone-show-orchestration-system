package lapr4.presentation.figurecatalogue;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.figurecatalogue.application.AddFigureCatalogueController;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.presentation.customers.CustomerDTOPrinter;
import lapr4.presentation.figurecategories.FigureCategoryDTOPrinter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AddFigureCatalogueUI extends AbstractUI {

    private final AddFigureCatalogueController controller = new AddFigureCatalogueController();

    @Override
    protected boolean doShow() {
        final String code = Console.readLine("Figure Code: ");
        final String descriptionString = Console.readLine("Description: ");
        final String figureVersionString = Console.readLine("Figure Version: ");
        final String figureTypeString = Console.readLine("Figure Type (Static/Dynamic): ").trim();
        System.out.println("Choose a customer: \n");
        final CustomerDTO customer = customerSelection();
        System.out.println("Choose a figureCategory: \n");
        final FigureCategoryDTO figureCategory = categorySelection();
        final String keywordsInput = Console.readLine("Keywords (separated by commas): ");
        final Set<String> keywords = Arrays.stream(keywordsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
        final String dslDescriptionString = Console.readLine("DSL Description (file): ");
        final String dslVersionString = Console.readLine("DSL Version: ");

        try {
            FigureDTO figure = controller.addFigureToCatalogue(descriptionString, dslDescriptionString, dslVersionString, code, figureTypeString, figureVersionString, customer, figureCategory, keywords, true);
            showFigureResult(figure);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }    catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That figure code is already in use.");
        } catch (IOException e) {
            System.out.println("\nError importing file: " + e.getMessage());
        }

        return false;
    }

    private CustomerDTO customerSelection() {
        Iterable<CustomerDTO> customers = controller.allCustomers();

        if (!customers.iterator().hasNext()) {
            System.out.println("There are no customers");
        } else {
            CustomerDTOPrinter printer = new CustomerDTOPrinter();
            final SelectWidget<CustomerDTO> selector = new SelectWidget<>(printer.header(), customers, printer);
            selector.show();
            return selector.selectedElement();
        }

        return null;
    }

    private FigureCategoryDTO categorySelection() {
        Iterable<FigureCategoryDTO> categories = controller.allActiveCategories();

        if (!categories.iterator().hasNext()) {
            System.out.println("There are no categories");
        } else {
            FigureCategoryDTOPrinter printer = new FigureCategoryDTOPrinter();
            final SelectWidget<FigureCategoryDTO> selector = new SelectWidget<>(printer.header(), categories, printer);
            selector.show();
            return selector.selectedElement();
        }

        return null;
    }

    @Override
    public String headline() {
        return "Add a Figure to the Catalogue";
    }

    private void showFigureResult(FigureDTO dto) {
        System.out.println("\n=== Registration Successful ===");

        FigureDTOPrinter printer = new FigureDTOPrinter();
        System.out.println(printer.header());
        printer.visit(dto);
    }

}
