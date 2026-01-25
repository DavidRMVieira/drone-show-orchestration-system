package lapr4.app.customer.console.presentation;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import lapr4.app.customer.console.authz.CredentialStore;
import lapr4.showproposal.application.ListScheduledShowsProxyController;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;


/**
 * UI for listing scheduled shows.
 * Allows the user to view all scheduled show proposals
 *
 */
@SuppressWarnings("java:S106")
public class ListScheduledShowsUI extends AbstractListUI<ShowProposalDTO> {
    private static final Logger LOGGER = LogManager.getLogger(ListScheduledShowsUI.class);

    private final ListScheduledShowsProxyController controller = new ListScheduledShowsProxyController();

    @Override
    protected Iterable<ShowProposalDTO> elements() {
        try {
            Iterable<ShowProposalDTO> scheduledShows = controller.listCustomerScheduledShows(CredentialStore.getUsername(), CredentialStore.getPassword());

            if (!scheduledShows.iterator().hasNext()) {
                System.out.println("There are no schedule show proposals yet.");
            }

            return scheduledShows;
        } catch (final IOException e) {
            System.out.println("Problems with network connection");
            LOGGER.debug(e);
            return Collections.emptyList();

        } catch (final ConcurrencyException e) {
            System.out.println("Problems with Data integrity");
            return Collections.emptyList();

        } catch (final FailedRequestException e) {
            System.out.println("Problems with request, check message " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    protected Visitor<ShowProposalDTO> elementPrinter() {
        return new ShowProposalDTOPrinter();
    }

    @Override
    protected String elementName() {
        return "Show";
    }

    @Override
    protected String listHeader() {
        return String.format(
                "#  %-6s | %-20s | %-11s | %-9s | %-11s | %-6s | %-6s | %-20s | %-25s | %-30s%n",
                "ID", "DATE", "DURATION", "DRONES", "INSURANCE", "LAT", "LNG",
                "STATE", "REP EMAIL", "VIDEO SIMULATION"
        ) + "\n";
    }

    @Override
    protected String emptyMessage() {
        return "No scheduled shows found\n";
    }

    @Override
    public String headline() {
        return "List Scheduled Shows";
    }
}

