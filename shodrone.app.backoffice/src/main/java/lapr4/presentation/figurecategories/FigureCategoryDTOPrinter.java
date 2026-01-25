package lapr4.presentation.figurecategories;

import eapli.framework.visitor.Visitor;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

public class FigureCategoryDTOPrinter implements Visitor<FigureCategoryDTO> {

    @Override
    public void visit(FigureCategoryDTO category) {
        System.out.printf(
                "%-30s%-50s%-20s%n",
                category.getName(),
                category.getDescription(),
                category.isActive() ? "Active" : "Inactive"
        );
    }

    public String header() {
        return String.format(
                "%-30s%-50s%-20s%n",
                "NAME", "DESCRIPTION", "STATE"
                );
    }
}
