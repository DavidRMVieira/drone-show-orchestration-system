package lapr4.presentation.integrations.proposaltemplate;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import lapr4.integrations.proposaltemplate.application.RegisterProposalTemplateController;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;

@SuppressWarnings("java:S106")
public class RegisterProposalTemplateUI extends AbstractUI {

    private final RegisterProposalTemplateController controller = new RegisterProposalTemplateController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Register Proposal Template ---");

        final String proposalTemplateVersion = Console.readLine("Proposal Template Version: ");
        final String className = Console.readLine("Class Name: ");

        try {
            ProposalTemplate template = controller.registerProposalTemplate(proposalTemplateVersion, className);
            showImportResults(template);
        } catch (@SuppressWarnings("unused") final IntegrityViolationException e) {
            System.out.println("That version is already in use!");
        }

        return false;
    }

    private void showImportResults(ProposalTemplate template) {
        System.out.println("\n=== Template Results ===");

        System.out.println("Proposal Template Version: " + template.identity());
        System.out.println("Class Name: " + template.className());
    }

    @Override
    public String headline() {
        return "Register Proposal Template";
    }
}