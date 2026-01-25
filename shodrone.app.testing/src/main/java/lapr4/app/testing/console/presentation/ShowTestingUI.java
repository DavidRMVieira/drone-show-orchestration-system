package lapr4.app.testing.console.presentation;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.testingshow.application.ShowTestingController;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.testingshow.application.ShowTestingProxyController;
import lapr4.testingshow.csvprotocol.client.FailedRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;


/**
 * UI for testing a show.
 * Allows drone technicians to select a show to test in the simulator.
 */
@SuppressWarnings("java:S106")
public class ShowTestingUI extends AbstractUI {
    private static final Logger LOGGER = LogManager.getLogger(ShowTestingUI.class);

    private final ShowTestingController controller = new ShowTestingController();
    private final ShowTestingProxyController proxyController = new ShowTestingProxyController();

    @Override
    protected boolean doShow() {
        final var selectedShow = selectShow();

        if (selectedShow == null) {
            System.out.println("There are no shows to test or an error occurred.");
            return false;
        }

        System.out.println("\n=== Show Chosen for Testing Details ===\n");
        System.out.println(selectedShow.document());

        try {
            String option = Console.readLine("Start Testing (y/n)? ").toLowerCase();

            switch (option) {
                case "y":
                    List<String> result = proxyController.testShow(selectedShow);
                    if (result.isEmpty()) {
                        break;
                    }

                    System.out.println("\n === Show Testing Results ===\n");
                    for (String line : result) {
                        System.out.println(line);
                    }

                    break;
                case "n":
                    System.out.println("Show testing cancelled.");
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

    private ShowProposalDTO selectShow() {
        try {
            Iterable<ShowProposalDTO> testableShows = controller.listShows();

            if (!testableShows.iterator().hasNext()) {
                System.out.println("There are no shows that can be tested yet.");
                return null;
            }

            final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                    new ShowProposalDTOPrinter().header(), testableShows, new ShowProposalDTOPrinter());
            proposalSelector.show();

            return proposalSelector.selectedElement();

        } catch (final ConcurrencyException e) {
            System.out.println("Problems with Data integrity");
            return null;
        }
    }

    @Override
    public String headline() {
        return "Test a Show in the Simulator";
    }
}
