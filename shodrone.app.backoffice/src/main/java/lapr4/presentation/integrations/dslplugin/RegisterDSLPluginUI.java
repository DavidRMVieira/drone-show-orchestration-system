package lapr4.presentation.integrations.dslplugin;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.integrations.dslplugin.application.RegisterDSLPluginController;
import lapr4.integrations.dslplugin.domain.DSLPlugin;

@SuppressWarnings("java:S106")
public class RegisterDSLPluginUI extends AbstractUI {

    private final RegisterDSLPluginController controller = new RegisterDSLPluginController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Register DSL Plugin ---");

        final String dslVersion = Console.readLine("DSL Version: ");
        final String className = Console.readLine("Class Name: ");

        try {
            DSLPlugin plugin = controller.registerDSLPlugin(dslVersion, className);
            showImportResults(plugin);
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That version is already in use.");
        }

        return false;
    }

    private void showImportResults(DSLPlugin plugin) {
        System.out.println("\n=== Plugin Results ===");

        System.out.println("DSL Version: " + plugin.identity());
        System.out.println("Class Name: " + plugin.className());
    }

    @Override
    public String headline() {
        return "Register DSL Plugin";
    }
}