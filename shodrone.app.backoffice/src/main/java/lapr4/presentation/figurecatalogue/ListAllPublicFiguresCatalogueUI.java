package lapr4.presentation.figurecatalogue;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import lapr4.figurecatalogue.application.ListPublicFiguresCatalogueController;
import lapr4.figurecatalogue.dto.FigureDTO;

public class ListAllPublicFiguresCatalogueUI extends AbstractListUI<FigureDTO> {

    private final ListPublicFiguresCatalogueController controller = new ListPublicFiguresCatalogueController();

    @Override
    protected Iterable<FigureDTO> elements() {
        return controller.listAllPublicFigures();
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
        return "No public figures found in the Catalogue!\n";
    }

    @Override
    public String headline() {
        return "List Public Figures in the Catalogue";
    }
}
