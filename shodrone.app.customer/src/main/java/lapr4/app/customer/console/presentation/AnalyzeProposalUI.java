package lapr4.app.customer.console.presentation;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.app.customer.console.authz.CredentialStore;
import lapr4.showproposal.application.AnalyzeProposalProxyController;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * UI for analyzing a show proposal.
 * Allows the user to select a proposal and view its downloadable file.
 *
 */
@SuppressWarnings("java:S106")
public class AnalyzeProposalUI extends AbstractUI {
    private static final Logger LOGGER = LogManager.getLogger(AnalyzeProposalUI.class);

    private final AnalyzeProposalProxyController controller = new AnalyzeProposalProxyController();

    @Override
    protected boolean doShow() {
        final var selectedProposal = selectProposal();

        if (selectedProposal == null) {
            System.out.println("There are no show proposals to analyze or an error occurred.");
            return false;
        }

        try {
            showAnalysisFileLink(selectedProposal);
        } catch (IntegrityViolationException e) {
            System.out.println("Error: Could not analyze the show proposal. " + e.getMessage());
        }

        return false;
    }

    private ShowProposalDTO selectProposal() {
        try {
            Iterable<ShowProposalDTO> myShowProposals = controller.listRepresentativeShowProposalsAwaitingResponse(CredentialStore.getUsername(), CredentialStore.getPassword());

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

    @Override
    public String headline() {
        return "Analyze a Show Proposal";
    }
}
