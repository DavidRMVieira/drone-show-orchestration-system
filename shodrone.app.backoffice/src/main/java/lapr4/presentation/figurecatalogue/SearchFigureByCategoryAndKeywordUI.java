package lapr4.presentation.figurecatalogue;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import lapr4.figurecatalogue.application.SearchFigureCatalogueController;
import lapr4.figurecatalogue.dto.FigureDTO;

public class SearchFigureByCategoryAndKeywordUI extends AbstractListUI<FigureDTO> {

    private final SearchFigureCatalogueController controller = new SearchFigureCatalogueController();

    @Override
    protected Iterable<FigureDTO> elements() {
        String category = Console.readLine("FigureCategory: ");
        String keyword = Console.readLine("Keyword: ");
        return controller.searchByCategoryAndKeyword(category, keyword);
    }

    @Override
    protected Visitor<FigureDTO> elementPrinter() {
        return new FigureDTOPrinter();
    }

    @Override
    protected String elementName() {
        return "Figure";
    }

    @Override
    protected String listHeader() {
        return String.format(
                "#  %-10s%-40s%-10s%-10s%-30s%-15s%-40s%-40s%-5s",
                "CODE", "DESCRIPTION", "VERSION", "TYPE", "CLIENT (VAT)", "CATEGORY", "KEYWORDS", "DSL DESCRIPTION", "DSL VERSION"
        ) + "\n";
    }

    @Override
    protected String emptyMessage() {
        return "No figures with specified category and keyword found!\n";
    }

    @Override
    public String headline() {
        return "Figures by specified keyword and category";
    }
}
