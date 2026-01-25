package lapr4.app.customer.console.presentation;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.app.customer.console.authz.CredentialStore;
import lapr4.showproposal.application.GetShowInfoProxyController;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * UI for getting show information.
 * This class allows the user to select a show proposal and view its details.
 *
 */
@SuppressWarnings("java:S106")
public class GetShowInfoUI extends AbstractUI {
    private static final Logger LOGGER = LogManager.getLogger(GetShowInfoUI.class);

    private final GetShowInfoProxyController controller = new GetShowInfoProxyController();

    @Override
    protected boolean doShow() {
        final ShowProposalDTO selectedProposal = selectProposal();
        if (selectedProposal == null) {
            System.out.println("There are no show proposals to consult or an error occurred.");
            return false;
        }

        System.out.println("\n=== Show Info ===\n");
        System.out.printf("Show Proposal Number: %d\n", selectedProposal.id());
        System.out.printf("Date: %s\n", selectedProposal.date());
        System.out.printf("Duration: %d minutes\n", selectedProposal.duration());
        System.out.printf("Video Simulation: %s\n", selectedProposal.videoSimulation());
        System.out.printf("Number of Drones: %d\n", selectedProposal.numberOfDrones());
        System.out.printf("Insurance Amount: %.2f\n", selectedProposal.insuranceAmount());
        System.out.printf("Latitude: %.6f\n", selectedProposal.latitudeLocation());
        System.out.printf("Longitude: %.6f\n", selectedProposal.longitudeLocation());

        System.out.println("\n--- Drones in Show ---");
        for (DroneInShowDTO drone : selectedProposal.drones()) {
            System.out.printf("Drone Model: %s, Quantity: %d\n", drone.droneModelName(), drone.quantity());
        }

        System.out.println("\n--- Figures in Show ---");
        for (FigureInShowDTO figure : selectedProposal.figures()) {
            System.out.printf("Figure Code: %s, X: %.2f, Y: %.2f, Z: %.2f\n",
                    figure.figureCode(), figure.Xcoordinate(), figure.Ycoordinate(), figure.Zcoordinate());
        }

        return false;
    }

    private ShowProposalDTO selectProposal() {
        try {
            Iterable<ShowProposalDTO> proposals = controller.listCustomerShowProposals(CredentialStore.getUsername(), CredentialStore.getPassword());

            if (!proposals.iterator().hasNext()) {
                System.out.println("There are no shows to consult");
                return null;
            }

            final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                    new ShowProposalDTOPrinter().header(), proposals, new ShowProposalDTOPrinter());
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
        return "Get Details of Show Proposal";
    }
}
