package lapr4.presentation.figurecatalogue;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.figurecatalogue.application.DecommissionFigureCatalogueController;
import lapr4.figurecatalogue.dto.FigureDTO;

public class DecommissionFigureCatalogueUI extends AbstractUI {

    private final DecommissionFigureCatalogueController controller = new DecommissionFigureCatalogueController();

    @Override
    protected boolean doShow() {
        Iterable<FigureDTO> figures = controller.allFigures();

        if(!figures.iterator().hasNext()) {
            System.out.println("There are no figures");
        } else {
            FigureDTOPrinter printer = new FigureDTOPrinter();
            final SelectWidget<FigureDTO> selector = new SelectWidget<>(printer.header(), figures, printer);
            selector.show();
            final FigureDTO selectedFigure = selector.selectedElement();
            if (selectedFigure != null) {
                try {
                    controller.decommissionFigure(selectedFigure);
                    System.out.println("\n=== Decommission Successful ===");
                } catch (final ConcurrencyException ex) {
                    System.out.println("WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }

        return false;
    }

    @Override
    public String headline() {
        return "Decommission Figure from Catalogue";
    }
}
