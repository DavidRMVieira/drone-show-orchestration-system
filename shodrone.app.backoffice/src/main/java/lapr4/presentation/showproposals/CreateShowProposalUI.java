package lapr4.presentation.showproposals;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.customermanagement.dto.RepresentativeDTO;
import lapr4.presentation.customers.RepresentativeDTOPrinter;
import lapr4.presentation.showrequests.ShowRequestDTOPrinter;
import lapr4.showproposalmanagement.application.CreateShowProposalController;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

import java.util.Date;

@SuppressWarnings("java:S106")
public class CreateShowProposalUI extends AbstractUI {

    private final CreateShowProposalController controller = new CreateShowProposalController();

    @Override
    protected boolean doShow() {
        Iterable<ShowRequestDTO> showRequests = controller.listShowRequests();

        if (!showRequests.iterator().hasNext()) {
            System.out.println("There are no show requests");
            return false;
        }

        final SelectWidget<ShowRequestDTO> requestSelector = new SelectWidget<>(
                new ShowRequestDTOPrinter().header(), showRequests, new ShowRequestDTOPrinter());
        requestSelector.show();
        final ShowRequestDTO selectedRequest = requestSelector.selectedElement();

        if (selectedRequest == null) return false;

        boolean exclusive = Console.readBoolean("Should the proposal be exclusive to a representative? (y/n)");
        RepresentativeDTO selectedRepresentative = null;

        if (exclusive) {
            Iterable<RepresentativeDTO> representatives = controller.listCustomerRepresentatives(selectedRequest);
            if (!representatives.iterator().hasNext()) {
                System.out.println("No representatives available for this customer.");
                return false;
            }

            final SelectWidget<RepresentativeDTO> representativeSelector = new SelectWidget<>(
                    new RepresentativeDTOPrinter().header(), representatives, new RepresentativeDTOPrinter());
            representativeSelector.show();
            selectedRepresentative = representativeSelector.selectedElement();

            if (selectedRepresentative == null) {
                System.out.println("Operation cancelled.");
                return false;
            }
        }

        final double latitude = Console.readDouble("Latitude Location: ");
        final double longitude = Console.readDouble("Longitude Location: ");
        final int duration = Console.readInteger("Duration (minutes): ");
        final int numberDrones = Console.readInteger("Number of drones: ");
        final double insurance = Console.readDouble("Insurance amount: ");
        final Date date = Console.readDate("Date (yyyy/mm/dd): ");

        ShowProposalDTO proposalDTO = new ShowProposalDTO(date, duration, numberDrones, insurance, latitude, longitude);

        try {
            ShowProposalDTO result = controller.createShowProposal(proposalDTO, selectedRequest, selectedRepresentative);
            showRegistrationResult(result);
        } catch (IntegrityViolationException e) {
            System.out.println("Error: Could not create the show proposal. " + e.getMessage());
        }

        return false;
    }

    private void showRegistrationResult(ShowProposalDTO proposal) {
        System.out.println("\n=== Registration Successful ===");

        ShowProposalDTOPrinter printer = new ShowProposalDTOPrinter();
        System.out.println(printer.header());
        printer.visit(proposal);
    }

    @Override
    public String headline() {
        return "Create Show Proposal";
    }
}
