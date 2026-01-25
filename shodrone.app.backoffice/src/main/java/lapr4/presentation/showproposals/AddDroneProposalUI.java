package lapr4.presentation.showproposals;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.presentation.droneinventory.DroneModelDTOPrinter;
import lapr4.showproposalmanagement.application.AddDroneProposalController;
import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S106")
public class AddDroneProposalUI extends AbstractUI {

    private final AddDroneProposalController controller = new AddDroneProposalController();

    @Override
    protected boolean doShow() {
        Iterable<ShowProposalDTO> proposals = controller.listShowProposals();

        if (!proposals.iterator().hasNext()) {
            System.out.println("There are no show proposals yet.");
            return false;
        }

        final SelectWidget<ShowProposalDTO> proposalSelector = new SelectWidget<>(
                new ShowProposalDTOPrinter().header(), proposals, new ShowProposalDTOPrinter());
        proposalSelector.show();
        final ShowProposalDTO selectedProposal = proposalSelector.selectedElement();
        if (selectedProposal == null) { return false; }

        Iterable<DroneModelDTO> drones = controller.listDroneModels();

        if (!drones.iterator().hasNext()) {
            System.out.println("There are no drone models");
            return false;
        }

        final int option = Console.readInteger(
                "Do you want to:\n1 - Configure ALL drones for the proposal\n2 - Add ONE drone to the proposal\nChoose an option: ");

        if (option == 1) {
            return configureAllDrones(selectedProposal, drones);
        } else if (option == 2) {
            return addSingleDrone(selectedProposal, drones);
        } else {
            System.out.println("Invalid option.");
            return false;
        }

    }

    private boolean configureAllDrones(ShowProposalDTO selectedProposal, Iterable<DroneModelDTO> drones) {
        List<DroneInShowDTO> droneInShowDTOList = new ArrayList<>();

        while (true) {
            final SelectWidget<DroneModelDTO> droneSelector = new SelectWidget<>(
                    new DroneModelDTOPrinter().header(), drones, new DroneModelDTOPrinter());
            droneSelector.show();
            final DroneModelDTO selectedDrone = droneSelector.selectedElement();

            if (selectedDrone == null) {
                System.out.println("No drone selected. Cancelling...");
                return false;
            }

            final int quantity = Console.readInteger("Quantity for selected drone: ");

            boolean updated = false;
            for (int i = 0; i < droneInShowDTOList.size(); i++) {
                DroneInShowDTO existing = droneInShowDTOList.get(i);
                if (existing.droneModelName().equals(selectedDrone.getName())) {
                    int newQty = existing.quantity() + quantity;
                    droneInShowDTOList.set(i, new DroneInShowDTO(existing.droneModelName(), newQty));
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                droneInShowDTOList.add(new DroneInShowDTO(selectedDrone.getName(), quantity));
            }

            final String more = Console.readLine("Do you want to add another drone? (y/n): ");
            if (!more.equalsIgnoreCase("y")) {
                break;
            }
        }

        try {
            controller.configureDronesProposal(selectedProposal, droneInShowDTOList);
            System.out.println("\n=== Drones configured successfully ===");
            return true;
        } catch (IntegrityViolationException | IllegalArgumentException e) {
            System.out.println("Error configuring drones: " + e.getMessage());
        }

        return false;
    }

    private boolean addSingleDrone(ShowProposalDTO selectedProposal, Iterable<DroneModelDTO> drones) {
        final SelectWidget<DroneModelDTO> droneSelector = new SelectWidget<>(
                new DroneModelDTOPrinter().header(), drones, new DroneModelDTOPrinter());
        droneSelector.show();
        final DroneModelDTO selectedDrone = droneSelector.selectedElement();

        if (selectedDrone == null) return false;

        final int quantity = Console.readInteger("Quantity: ");

        try {
            controller.addDroneProposal(selectedProposal, selectedDrone, quantity);
            System.out.println("\n=== Drone added successfully ===");
            return true;
        } catch (IntegrityViolationException | IllegalArgumentException e) {
            System.out.println("Error adding drone: " + e.getMessage());
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add Drone Proposal";
    }

}
