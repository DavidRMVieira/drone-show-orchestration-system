package lapr4.presentation.integrations.dronelanguageplugin;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.integrations.dronelanguageplugin.application.RegisterDroneLanguagePluginController;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;

@SuppressWarnings("java:S106")
public class RegisterDroneLanguagePluginUI extends AbstractUI {

    private final RegisterDroneLanguagePluginController controller = new RegisterDroneLanguagePluginController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Register Drone Language Plugin ---");

        final String droneLanguageVersion = Console.readLine("Drone Language Version: ");
        final String className = Console.readLine("Class Name: ");

        try {
            DroneLanguagePlugin plugin = controller.registerDroneLanguagePlugin(droneLanguageVersion, className);
            showImportResults(plugin);
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That version is already in use.");
        }

        return false;
    }

    private void showImportResults(DroneLanguagePlugin plugin) {
        System.out.println("\n=== Plugin Results ===");

        System.out.println("Drone Language Version: " + plugin.identity());
        System.out.println("Class Name: " + plugin.className());
    }

    @Override
    public String headline() {
        return "Register Drone Language Plugin";
    }
}