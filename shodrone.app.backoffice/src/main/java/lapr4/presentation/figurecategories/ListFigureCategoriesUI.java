package lapr4.presentation.figurecategories;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import lapr4.figurecategorymanagement.application.ListFigureCategoryController;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

@SuppressWarnings({ "squid:S106" })
public class ListFigureCategoriesUI extends AbstractListUI<FigureCategoryDTO> {

    private final ListFigureCategoryController controller = new ListFigureCategoryController();

    @Override
    public String headline() {
        return "List Figure Categories";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<FigureCategoryDTO> elements() {
        return controller.listFigureCategories();
    }

    @Override
    protected Visitor<FigureCategoryDTO> elementPrinter() {
        return new FigureCategoryDTOPrinter();
    }

    @Override
    protected String elementName() {
        return "Category";
    }

    @Override
    protected String listHeader() {
        return String.format(
                "#  %-30s%-50s%-20s",
                "NAME", "DESCRIPTION", "STATE"
        ) + "\n";
    }

}