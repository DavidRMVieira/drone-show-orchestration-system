package lapr4.presentation.showproposals;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.presentation.figurecatalogue.FigureDTOPrinter;
import lapr4.showproposalmanagement.application.AddFigureProposalController;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S106")
public class AddFigureProposalUI extends AbstractUI {

    private final AddFigureProposalController controller = new AddFigureProposalController();

    @Override
    protected boolean doShow() {
        Iterable<ShowProposalDTO> proposals = controller.listShowProposals();

        if (!proposals.iterator().hasNext()) {
            System.out.println("There are no show proposals");
            return false;
        }

        final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                new ShowProposalDTOPrinter().header(), proposals, new ShowProposalDTOPrinter());
        proposalSelector.show();
        final ShowProposalDTO selectedProposal = proposalSelector.selectedElement();

        if (selectedProposal == null) return false;

        Iterable<FigureDTO> figures = controller.listFigureModels();

        if (!figures.iterator().hasNext()) {
            System.out.println("There are no figure models");
            return false;
        }

        final int option = Console.readInteger(
                "Do you want to:\n1 - Configure ALL figures for the proposal\n2 - Add ONE figure to the proposal\nChoose an option: ");

        if (option == 1) {
            return configureAllFigures(selectedProposal, figures);
        } else if (option == 2) {
            return addSingleFigure(selectedProposal, figures);
        } else {
            System.out.println("Invalid option.");
            return false;
        }
    }

    private boolean configureAllFigures(ShowProposalDTO selectedProposal, Iterable<FigureDTO> figures) {
        List<FigureInShowDTO> figureInShowDTOList = new ArrayList<>();

        while (true) {
            final SelectWidget<FigureDTO> figureSelector = new SelectWidget<>(
                    new FigureDTOPrinter().header(), figures, new FigureDTOPrinter());
            figureSelector.show();
            final FigureDTO selectedFigure = figureSelector.selectedElement();

            if (selectedFigure == null) {
                System.out.println("No figure selected. Cancelling...");
                return false;
            }

            final double coordinateX = Console.readDouble("Coordinate X: ");
            final double coordinateY = Console.readDouble("Coordinate Y: ");
            final double coordinateZ = Console.readDouble("Coordinate Z: ");

            boolean updated = false;
            for (int i = 0; i < figureInShowDTOList.size(); i++) {
                FigureInShowDTO existing = figureInShowDTOList.get(i);
                if (existing.figureCode().equals(selectedFigure.getCode())) {
                    figureInShowDTOList.set(i, new FigureInShowDTO(selectedFigure.getCode(), coordinateX, coordinateY, coordinateZ));
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                figureInShowDTOList.add(new FigureInShowDTO(selectedFigure.getCode(), coordinateX, coordinateY, coordinateZ));
            }

            final String more = Console.readLine("Do you want to add another figure? (y/n): ");
            if (!more.equalsIgnoreCase("y")) {
                break;
            }
        }

        try {
            controller.configureFiguresProposal(selectedProposal, figureInShowDTOList);
            System.out.println("\n=== Figures configured successfully ===");
            return true;
        } catch (IntegrityViolationException | IllegalArgumentException e) {
            System.out.println("Error configuring figures: " + e.getMessage());
        }

        return false;
    }

    private boolean addSingleFigure(ShowProposalDTO selectedProposal, Iterable<FigureDTO> figures) {
        final SelectWidget<FigureDTO> figureSelector = new SelectWidget<>(
                new FigureDTOPrinter().header(), figures, new FigureDTOPrinter());
        figureSelector.show();
        final FigureDTO selectedFigure = figureSelector.selectedElement();

        if (selectedFigure == null) return false;

        final double coordinateX = Console.readDouble("Coordinate X: ");
        final double coordinateY = Console.readDouble("Coordinate Y: ");
        final double coordinateZ = Console.readDouble("Coordinate Z: ");

        try {
            controller.addFigureProposal(selectedProposal, selectedFigure, coordinateX, coordinateY, coordinateZ);
            System.out.println("\n=== Figure added successfully ===");
            return true;
        } catch (IntegrityViolationException | IllegalArgumentException e) {
            System.out.println("Error adding figure: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add Figure Proposal";
    }
}
