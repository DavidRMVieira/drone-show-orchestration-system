package lapr4.app.customer.console.presentation;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import lapr4.app.customer.console.authz.CredentialStore;


/**
 * Main menu for the Customer App.
 * This menu allows users to navigate through different functionalities such as analyzing show proposals,
 * listing scheduled shows, and getting show information.
 * It also includes a separator and an exit option.
 */
public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";

    private static final String RETURN_LABEL = "Return ";

    private static final int EXIT_OPTION = 0;

    // MAIN MENU
    private static final int CA_OPTION = 1;

    // Customer App
    private static final int ANALYZE_SHOW_PROPOSAL_OPTION = 1;
    private static final int LIST_SCHEDULED_SHOWS_OPTION = 2;
    private static final int GET_SHOW_INFO_OPTION = 3;


    /**
     * @return true if the user selected the exit option
     */
    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    /**
     * @return true if the user selected the exit option
     */
    @Override
    public boolean doShow() {
        final Menu menu = buildMainMenu();
        final var renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        return renderer.render();
    }

    /**
     * Draws the title of the form.
     */
    @Override
    public String headline() {
        return "SHODRONE [ " + CredentialStore.getUsername() + " ]";
    }

    /**
     * Builds the main menu for the Customer App.
     *
     * @return the constructed main menu
     */
    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();

        final EvaluateProposalAction ui = new EvaluateProposalAction();
        ui.execute();

        mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));

        final Menu customerAppMenu = buildCustomerAppMenu();
        mainMenu.addSubMenu(CA_OPTION, customerAppMenu);

        mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    /**
     * Builds the Customer App menu with options for analyzing show proposals,
     * listing scheduled shows, and getting show information.
     *
     * @return the constructed Customer App menu
     */
    private Menu buildCustomerAppMenu() {
        final Menu menu = new Menu("Customer App >");

        menu.addItem(ANALYZE_SHOW_PROPOSAL_OPTION, "Analyze a Show Proposal", new AnalyzeProposalAction());
        menu.addItem(LIST_SCHEDULED_SHOWS_OPTION, "List Scheduled Shows", new ListScheduledShowsAction());
        menu.addItem(GET_SHOW_INFO_OPTION, "Get the Details of a Show", new GetShowInfoAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

}
