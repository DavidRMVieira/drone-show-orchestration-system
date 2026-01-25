package lapr4.presentation.figurecatalogue;

import eapli.framework.visitor.Visitor;
import lapr4.figurecatalogue.dto.FigureDTO;

public class FigureDTOPrinter implements Visitor<FigureDTO> {

    @Override
    public void visit(FigureDTO visitee) {
        String client = (visitee.getCustomer() != null) ? visitee.getCustomer() : "None";
        String keywords = String.join(", ", visitee.getKeywords());

        System.out.printf(
                "%-10s%-40s%-10s%-10s%-30s%-15s%-40s%-40s%-10s%n",
                visitee.getCode(),
                visitee.getDescription(),
                visitee.getFigureVersion(),
                visitee.getType(),
                client,
                visitee.getFigureCategory(),
                keywords,
                visitee.getDslDescription(),
                visitee.getDslVersion()
        );
    }

    public String header() {
        return String.format(
                "%-10s%-40s%-10s%-10s%-30s%-15s%-40s%-40s%-10s%n",
                "CODE", "DESCRIPTION", "VERSION", "TYPE", "CUSTOMER (VAT)",
                "CATEGORY", "KEYWORDS", "DSL DESCRIPTION", "DSL VERSION"
        );
    }
}