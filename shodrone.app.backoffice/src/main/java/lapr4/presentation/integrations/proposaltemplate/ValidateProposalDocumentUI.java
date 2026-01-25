package lapr4.presentation.integrations.proposaltemplate;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.integrations.proposaltemplate.application.ValidateProposalDocumentController;
import lapr4.presentation.showproposals.ShowProposalDTOPrinter;
import lapr4.integrations.proposaltemplate.domain.Language;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.io.IOException;

@SuppressWarnings("java:S106")
public class ValidateProposalDocumentUI extends AbstractUI {

    private final ValidateProposalDocumentController controller = new ValidateProposalDocumentController();

    @Override
    protected boolean doShow() {
        System.out.println("\n--- Validate Proposal Document ---");

        Iterable<ShowProposalDTO> showProposals = controller.listShowProposals();

        ShowProposalDTOPrinter printer = new ShowProposalDTOPrinter();
        final SelectWidget<ShowProposalDTO> selector = new SelectWidget<>(printer.header(), showProposals, printer);
        selector.show();
        final ShowProposalDTO selectedShowProposal = selector.selectedElement();

        final String templateVersion = Console.readLine("Proposal Template Version: ");
        final String language = selectLanguage();

            if (selectedShowProposal != null) {
                try {
                    String document = controller.validateProposalDocument(selectedShowProposal, language, templateVersion);
                    System.out.println(document);
                } catch (IOException e) {
                    System.out.println("\n" + e.getMessage());
                } catch (final IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

        return false;
    }

    private String selectLanguage() {
        System.out.println("Language Available:");
        for (final Language type : Language.values()) {
            System.out.println("\t" + type.toString());
        }

        do {
            try {
                final String type = Console.readLine("Language: ").toUpperCase();
                return Language.valueOf(type).toString();
            } catch (final IllegalArgumentException e) {
                System.out.println("Please try again. Enter a valid language.");
            }
        } while (true);
    }

    @Override
    public String headline() {
        return "Validate Proposal Document";
    }
}