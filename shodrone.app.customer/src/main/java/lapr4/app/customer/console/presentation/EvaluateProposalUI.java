package lapr4.app.customer.console.presentation;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.app.customer.console.authz.CredentialStore;
import lapr4.showproposal.application.AnalyzeProposalProxyController;
import lapr4.showproposal.application.EvaluateProposalProxyController;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * UI for evaluating proposals.
 * Allows the user to accept or reject a proposal after reviewing its document.
 *
 */
@SuppressWarnings("java:S106")
public class EvaluateProposalUI extends AbstractUI {
    private static final Logger LOGGER = LogManager.getLogger(EvaluateProposalUI.class);

    private final EvaluateProposalProxyController evaluateController = new EvaluateProposalProxyController();
    private final AnalyzeProposalProxyController analyzeController = new AnalyzeProposalProxyController();

    @Override
    protected boolean doShow() {
        final ShowProposalDTO selectedProposal = selectProposal();
        if (selectedProposal == null) { return false; }

        showAnalysisFileLink(selectedProposal);

        try {

            String option = Console.readLine("Accept (A) or Reject (R)? ").toUpperCase();

            String result;
            switch (option) {
                case "A":
                    result = evaluateController.acceptProposal(CredentialStore.getUsername(), CredentialStore.getPassword(), selectedProposal);

                    System.out.println(result);
                    break;
                case "R":
                    String feedback = Console.readLine("Please provide a reason for rejection: ");
                    result = evaluateController.rejectProposal(CredentialStore.getUsername(), CredentialStore.getPassword(), selectedProposal, feedback);

                    System.out.println(result);
                    break;
                default:
                    System.out.println("Invalid option. No action was taken.");
                    break;
            }
        } catch (final IOException e) {
            System.out.println("Problems with network connection");
            LOGGER.debug(e);
        } catch (final ConcurrencyException e) {
            System.out.println("Problems with Data integrity");
        } catch (final FailedRequestException e) {
            System.out.println("Problems with request, check message " + e.getMessage());
        }

        return false;
    }

    private void showAnalysisFileLink(ShowProposalDTO proposal) {
        System.out.println("\n=== Show Proposal Analysis ===\n");
        System.out.println("Document link: " + proposal.documentLink());
        // Try to open the file automatically if it exists
        if (proposal.documentLink() != null && !proposal.documentLink().equals("N/A") && !proposal.documentLink().equals("[ERROR_CREATING_DOCUMENT_FILE]")) {
            try {
                java.io.File file = new java.io.File(proposal.documentLink());
                if (file.exists()) {
                    if (java.awt.Desktop.isDesktopSupported()) {
                        java.awt.Desktop.getDesktop().open(file);
                        System.out.println("(The document was opened automatically.)");
                    } else {
                        System.out.println("(Desktop is not supported to open files automatically.)");
                    }
                } else {
                    System.out.println("(File not found: " + proposal.documentLink() + ")");
                }
            } catch (Exception e) {
                System.out.println("(Error trying to open the file: " + e.getMessage() + ")");
            }
        }
    }

    private ShowProposalDTO selectProposal() {
        try {

            Iterable<ShowProposalDTO> myShowProposals = analyzeController.listRepresentativeShowProposalsAwaitingResponse(CredentialStore.getUsername(), CredentialStore.getPassword());

            if (!myShowProposals.iterator().hasNext()) {
                System.out.println("There are no show proposals yet.");
                return null;
            }

            final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                    new ShowProposalDTOPrinter().header(), myShowProposals, new ShowProposalDTOPrinter());
            proposalSelector.show();

            return proposalSelector.selectedElement();

        } catch (final IOException e) {
            System.out.println("Problems with network connection");
            LOGGER.debug(e);
            return null;

        } catch (final ConcurrencyException e) {
            System.out.println("Problems with Data integrity");
            return null;

        } catch (final FailedRequestException e) {
            System.out.println("Problems with request, check message " + e.getMessage());
            return null;
        }
    }

    @Override
    public String headline() {
        return "Evaluate Proposal";
    }
}
