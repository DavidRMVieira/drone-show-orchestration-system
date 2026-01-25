package lapr4.presentation.showproposals;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.showproposalmanagement.application.AddVideoSimulationProposalController;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;


@SuppressWarnings("java:S106")
public class AddVideoSimulationProposalUI extends AbstractUI {

    private final AddVideoSimulationProposalController controller = new AddVideoSimulationProposalController();

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
        if (selectedProposal == null) { return false; }

        final String video = Console.readLine("Video Simulation Link: ");

        try {
            controller.addVideoSimulationProposal(selectedProposal, video);
            System.out.println("\n=== Video added successfully ===");
            return true;
        } catch (IntegrityViolationException | IllegalArgumentException e) {
            System.out.println("Error adding video: " + e.getMessage());
        }

        return false;

    }

    @Override
    public String headline() {
        return "Add Video Simulation Proposal";
    }

}
