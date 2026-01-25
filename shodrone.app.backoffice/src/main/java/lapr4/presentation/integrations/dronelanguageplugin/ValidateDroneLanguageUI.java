package lapr4.presentation.integrations.dronelanguageplugin;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.integrations.dronelanguageplugin.application.ValidateDroneLanguageController;
import lapr4.integrations.dronelanguageplugin.dto.DroneLanguage;
import lapr4.integrations.dronelanguageplugin.dto.Instruction;

import java.io.IOException;

@SuppressWarnings("java:S106")
public class ValidateDroneLanguageUI extends AbstractUI {

    private final ValidateDroneLanguageController controller = new ValidateDroneLanguageController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Import Drone Language ---");

        final String droneLanguageVersion = Console.readLine("Drone Language Version: ");
        final String filename = Console.readLine("File path: ");

        try {
            DroneLanguage importedDroneLanguage = controller.validateDroneLanguage(droneLanguageVersion, filename);
            showImportResults(importedDroneLanguage);
        } catch (IOException e) {
            System.out.println("\nError importing file: " + e.getMessage());
        } catch (final IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void showImportResults(DroneLanguage importedDroneLanguage) {
        System.out.println("\n=== Import Results ===");

        for (Instruction instruction : importedDroneLanguage.instructions()) {
            System.out.println(instruction);
        }

        System.out.println("\n=== End of Results ===");
    }

    @Override
    public String headline() {
        return "Import Drone Language";
    }

}
