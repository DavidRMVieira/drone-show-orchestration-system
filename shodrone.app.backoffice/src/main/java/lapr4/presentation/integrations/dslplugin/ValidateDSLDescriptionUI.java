package lapr4.presentation.integrations.dslplugin;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.integrations.dslplugin.application.ValidateDSLDescriptionController;
import lapr4.integrations.dslplugin.dto.DSLDescription;
import lapr4.integrations.dslplugin.dto.GeometricFigure;
import lapr4.integrations.dslplugin.dto.Method;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("java:S106")
public class ValidateDSLDescriptionUI extends AbstractUI {

    private final ValidateDSLDescriptionController controller = new ValidateDSLDescriptionController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Import DSL Description ---");

        final String dslVersion = Console.readLine("DSL version: ");
        final String filename = Console.readLine("File Path: ");

        try {
            DSLDescription importedDSL = controller.validateDSL(dslVersion, filename);
            showImportResults(importedDSL);
        } catch (IOException e) {
            System.out.println("\nError importing file: " + e.getMessage());
        } catch (final IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void showImportResults(DSLDescription importedDSL) {
        System.out.println("\n=== Import Results ===");

        for (Map.Entry<Method, GeometricFigure> entry : importedDSL.instructions().entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }

        System.out.println("\n=== End of Results ===");
    }

    @Override
    public String headline() {
        return "Import DSL Description";
    }
}