package lapr4.app.testing.console.presentation;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import lapr4.Application;
import lapr4.presentation.authorization.MyUserMenu;
import lapr4.usermanagement.domain.ShodroneRoles;


/**
 * Main menu for the Testing App.
 * This menu allows drone technicians to access features for testing shows.
 * It also includes separators and an exit option.
 */
public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    private static final String RETURN_LABEL = "Return ";

    private static final int EXIT_OPTION = 0;

    // MAIN MENU
    private static final int MY_USER_OPTION = 1;
    private static final int TA_OPTION = 2;

    // Testing App
    private static final int SHOW_TESTING_OPTION = 1;


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
        return authz.session().map(s -> "SHODRONE [ " + s.authenticatedUser().identity() + " ]")
                .orElse("SHODRONE [ ==Anonymous== ]");
    }

    /**
     * Builds the main menu for the Testing App.
     *
     * @return the constructed main menu
     */
    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();

        final Menu myUserMenu = new MyUserMenu();
        mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);

        if (!authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.DRONE_TECH)) {
            System.out.println("Warning: You are not authorized to access the Testing App features.");
            return mainMenu;
        }

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        final Menu testingAppMenu = buildTestingAppMenu();
        mainMenu.addSubMenu(TA_OPTION, testingAppMenu);

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    /**
     * Builds the Testing App menu with an option for testing shows.
     *
     * @return the constructed Testing App menu
     */
    private Menu buildTestingAppMenu() {
        final Menu menu = new Menu("Testing App >");

        menu.addItem(SHOW_TESTING_OPTION, "Test a Show in the Simulator", new ShowTestingAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

}
