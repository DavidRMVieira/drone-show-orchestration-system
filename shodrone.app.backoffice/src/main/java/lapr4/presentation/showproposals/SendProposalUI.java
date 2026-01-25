package lapr4.presentation.showproposals;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.showproposalmanagement.application.SendProposalController;
import lapr4.integrations.proposaltemplate.domain.Language;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.io.IOException;

public class SendProposalUI extends AbstractUI {

    private final SendProposalController controller = new SendProposalController();

    @Override
    protected boolean doShow() {
        Iterable<ShowProposalDTO> showProposals =controller.allShowProposals();

        if (!showProposals.iterator().hasNext()) {
            System.out.println("No show proposals found!");
        } else {
            ShowProposalDTOPrinter printer = new ShowProposalDTOPrinter();
            final SelectWidget<ShowProposalDTO> selector = new SelectWidget<>(printer.header(), showProposals, printer);
            selector.show();
            final ShowProposalDTO selectedShowProposal = selector.selectedElement();

            final String language = selectLanguage();
            final String proposalTemplateVersion = Console.readLine("Proposal Template Version: ");

            if (selectedShowProposal != null) {
                try {
                    controller.sendProposal(selectedShowProposal, language, proposalTemplateVersion);
                    System.out.println("Proposal sent successfully.");
                } catch (final ConcurrencyException ex) {
                    System.out.println("WARNING: That entity has already been changed or deleted since you last read it");
                } catch (IOException e) {
                    System.out.println("\n" + e.getMessage());
                } catch (final IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
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
        return "Send proposal to Customer";
    }
}
