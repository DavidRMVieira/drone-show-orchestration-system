package lapr4.presentation.showproposals;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.showproposalmanagement.application.AcceptProposalByCollaboratorController;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

public class AcceptProposalByCollaboratorUI extends AbstractUI {

    private final AcceptProposalByCollaboratorController controller = new AcceptProposalByCollaboratorController();

    @Override
    protected boolean doShow() {
        Iterable<ShowProposalDTO> pendingProposalsAcceptedByCustomer = controller.checkPendingProposalAcceptedByCustomer();

        if (!pendingProposalsAcceptedByCustomer.iterator().hasNext()) {
            System.out.println("There are no pending proposals");
            return false;
        }

        final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                new ShowProposalDTOPrinter().header(), pendingProposalsAcceptedByCustomer, new ShowProposalDTOPrinter());
        proposalSelector.show();
        final ShowProposalDTO selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) { return false; }

        System.out.println("\n=== Show Proposal Accepted By Customer ===\n");
        System.out.println(selectedProposal.document());

        String option = Console.readLine("Accept (A)? ").toUpperCase();

        if (option.equals("A")) {
            controller.acceptProposalByCollaborator(selectedProposal);
            System.out.println("Proposal accepted successfully.");
        }

        return false;
    }

    @Override
    public String headline() {
        return "Accept Proposal by Collaborator";
    }
}