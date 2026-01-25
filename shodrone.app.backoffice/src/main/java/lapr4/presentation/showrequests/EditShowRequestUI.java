package lapr4.presentation.showrequests;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lapr4.showrequestmanagement.application.EditShowRequestController;
import lapr4.showrequestmanagement.domain.ShowRequestState;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

import java.util.Date;

public class EditShowRequestUI extends AbstractUI {

    private final EditShowRequestController controller = new EditShowRequestController();

    @Override
    protected boolean doShow() {
        final Iterable<ShowRequestDTO> showRequests = controller.allShowRequests();

        if (!showRequests.iterator().hasNext()) {
            System.out.println("There are no show requests");
        } else {
            ShowRequestDTOPrinter printer = new ShowRequestDTOPrinter();
            final SelectWidget<ShowRequestDTO> selector = new SelectWidget<>(printer.header(), showRequests, printer);
            selector.show();
            ShowRequestDTO selectedShowRequest = selector.selectedElement();

            if (selectedShowRequest != null) {
                try {
                    System.out.println("Place: " + selectedShowRequest.getPlace());
                    boolean editPlace = Console.readBoolean("Do you want to change the place? (y/n)");
                    if (editPlace) {
                        String newName = Console.readLine("New Place: ");
                        selectedShowRequest = controller.changeShowRequestPlace(newName, selectedShowRequest);
                    }
                    System.out.println("Date: " + selectedShowRequest.getDate());
                    boolean changeDate = Console.readBoolean("Do you want to change the date? (y/n)");
                    if (changeDate) {
                        Date newDate = Console.readDate("New Date: ");
                        selectedShowRequest = controller.changeShowRequestDate(newDate, selectedShowRequest);
                    }
                    System.out.println("Duration: " + selectedShowRequest.getDuration());
                    boolean changeDuration = Console.readBoolean("Do you want to change the duration? (y/n)");
                    if (changeDuration) {
                        int newDuration = Console.readInteger("New Duration: ");
                        selectedShowRequest = controller.changeShowRequestDuration(newDuration, selectedShowRequest);
                    }
                    System.out.println("State: " + selectedShowRequest.getState());
                    boolean changeState = Console.readBoolean("Do you want to change the state? (y/n)");
                    if (changeState) {
                        ShowRequestState newState = selectShowRequestState();
                        selectedShowRequest = controller.changeShowRequestState(newState, selectedShowRequest);
                    }

                    showRegistrationResult(selectedShowRequest);
                } catch (@SuppressWarnings("unused") final ConcurrencyException ex) {
                    System.out.println(
                            "WARNING: That entity has already been changed or deleted since you last read it");
                }
            }
        }

        return false;
    }

    private void showRegistrationResult(ShowRequestDTO showRequest) {
        System.out.println("\n=== Changes successful ===");

        ShowRequestDTOPrinter printer = new ShowRequestDTOPrinter();
        System.out.println(printer.header());
        printer.visit(showRequest);
    }

    @Override
    public String headline() {
        return "Edit Show Request";
    }

    private ShowRequestState selectShowRequestState() {
        System.out.println("Show Request State Available:");
        for (final ShowRequestState type : ShowRequestState.values()) {
            System.out.println("\t" + type.toString());
        }

        do {
            try {
                final String type = Console.readLine("Show Request State: ").toUpperCase();
                return ShowRequestState.valueOf(type);
            } catch (final IllegalArgumentException e) {
                System.out.println("Please try again. Enter a valid show request state.");
            }
        } while (true);

    }
}
