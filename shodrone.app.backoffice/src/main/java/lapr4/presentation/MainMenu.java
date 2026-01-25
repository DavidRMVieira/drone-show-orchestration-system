package lapr4.presentation;

import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;
import lapr4.Application;
import lapr4.presentation.authorization.*;
import lapr4.presentation.customers.RegisterCustomerAction;
import lapr4.presentation.figurecatalogue.*;
import lapr4.presentation.figurecategories.*;
import lapr4.presentation.integrations.dronelanguageplugin.ValidateDroneLanguageAction;
import lapr4.presentation.integrations.dronelanguageplugin.RegisterDroneLanguagePluginAction;
import lapr4.presentation.integrations.dslplugin.RegisterDSLPluginAction;
import lapr4.presentation.integrations.dslplugin.ValidateDSLDescriptionAction;
import lapr4.presentation.integrations.proposaltemplate.RegisterProposalTemplateAction;
import lapr4.presentation.integrations.proposaltemplate.ValidateProposalDocumentAction;
import lapr4.presentation.showproposals.*;
import lapr4.presentation.showrequests.EditShowRequestAction;
import lapr4.presentation.showrequests.ListShowRequestCustomerAction;
import lapr4.presentation.showrequests.RegisterShowRequestAction;
import lapr4.presentation.users.AddUserAction;
import lapr4.presentation.users.DisableUserAction;
import lapr4.presentation.users.EnableUserAction;
import lapr4.presentation.users.ListUsersAction;
import lapr4.usermanagement.domain.ShodroneRoles;

public class MainMenu extends AbstractUI {

    private static final String RETURN_LABEL = "Return ";

    private static final int EXIT_OPTION = 0;

    // USERS
    private static final int ADD_USER_OPTION = 1;
    private static final int LIST_USERS_OPTION = 2;
    private static final int DISABLE_USER_OPTION = 3;
    private static final int ENABLE_USER_OPTION = 4;

    // CUSTOMERS
    private static final int REGISTER_CUSTOMER_OPTION = 1;

    // SHOW REQUESTS
    private static final int REGISTER_SHOW_REQUEST_OPTION = 1;
    private static final int EDIT_SHOW_RESPONSE_OPTION = 2;
    private static final int LIST_SHOW_REQUESTS_CUSTOMER_OPTION1 = 3;
    private static final int LIST_SHOW_REQUESTS_CUSTOMER_OPTION2 = 4;

    // FIGURE CATEGORY
    private static final int ADD_FIGURE_CATEGORY_OPTION = 1;
    private static final int EDIT_FIGURE_CATEGORY_OPTION = 2;
    private static final int LIST_FIGURE_CATEGORY_OPTION = 3;
    private static final int ACTIVATE_FIGURE_CATEGORY_OPTION = 4;
    private static final int INACTIVATE_FIGURE_CATEGORY_OPTION = 5;

    // CATALOGUE
    private static final int LIST_PUBLIC_FIGURES_OPTION = 1;
    private static final int SEARCH_BY_KEYWORD_AND_CATEGORY_OPTION = 2;
    private static final int SEARCH_BY_KEYWORD_OPTION = 3;
    private static final int SEARCH_BY_CATEGORY_OPTION = 4;
    private static final int ADD_FIGURE_OPTION = 5;
    private static final int DECOMMISSION_FIGURE_OPTION = 6;

    // SHOW PROPOSALS
    private static final int CREATE_SHOW_PROPOSAL_OPTION = 1;
    private static final int ADD_DRONE_PROPOSAL_OPTION = 2;
    private static final int ADD_FIGURE_PROPOSAL_OPTION = 3;
    private static final int ADD_VIDEO_PROPOSAL_OPTION = 4;
    private static final int SEND_PROPOSAL_OPTION = 5;
    private static final int EVALUATE_PROPOSAL_OPTION = 10;

    // INTEGRATIONS
    private static final int REGISTER_DSL_PLUGIN = 1;
    private static final int REGISTER_DRONE_LANGUAGE_PLUGIN = 2;
    private static final int REGISTER_PROPOSAL_TEMPLATE = 3;
    private static final int IMPORT_DSL = 4;
    private static final int IMPORT_DRONE_DSL = 5;
    private static final int IMPORT_PROPOSAL_TEMPLATES = 6;

    // MAIN MENU
    private static final int MY_USER_OPTION = 1;
    private static final int USERS_OPTION = 2;
    private static final int CUSTOMERS_OPTION = 3;
    private static final int SHOW_REQUEST_OPTION = 4;
    private static final int CATALOGUE_OPTION = 5;
    private static final int FIGURE_CATEGORY_OPTION = 6;
    private static final int SHOW_PROPOSAL_OPTION = 7;
    private static final int INTEGRATION_OPTION = 8;

    private static final String SEPARATOR_LABEL = "--------------";

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

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
        final MenuRenderer renderer;
        if (Application.settings().isMenuLayoutHorizontal()) {
            renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        } else {
            renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        }
        return renderer.render();
    }

    @Override
    public String headline() {

        return authz.session().map(s -> "SHODRONE [ " + s.authenticatedUser().identity() + " ]")
                .orElse("SHODRONE [ ==Anonymous== ]");
    }

    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();

        final Menu myUserMenu = new MyUserMenu();
        mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.CRM_COLLABORATOR)) {
            final AcceptProposalByCollaboratorAction ui = new AcceptProposalByCollaboratorAction();
            ui.execute();
        }

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        final Menu usersMenu = buildUsersMenu();
        mainMenu.addSubMenu(USERS_OPTION, usersMenu);

        final Menu customersMenu = buildCustomersMenu();
        mainMenu.addSubMenu(CUSTOMERS_OPTION, customersMenu);

        final Menu showRequestsMenu = buildShowRequestsMenu();
        mainMenu.addSubMenu(SHOW_REQUEST_OPTION, showRequestsMenu);

        final Menu catalogueMenu = buildCatalogueMenu();
        mainMenu.addSubMenu(CATALOGUE_OPTION, catalogueMenu);

        final Menu figureCategoryMenu = buildFigureCategoryMenu();
        mainMenu.addSubMenu(FIGURE_CATEGORY_OPTION, figureCategoryMenu);

        final Menu showProposalMenu = buildShowProposalsMenu();
        mainMenu.addSubMenu(SHOW_PROPOSAL_OPTION, showProposalMenu);

        final Menu integrationsMenu = buildIntegrationsMenu();
        mainMenu.addSubMenu(INTEGRATION_OPTION, integrationsMenu);

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    private Menu buildUsersMenu() {
        final Menu menu = new Menu("Users >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.ADMIN)) {
            menu.addItem(ADD_USER_OPTION, "Add User", new AddUserAction());
            menu.addItem(LIST_USERS_OPTION, "List all Users", new ListUsersAction());
            menu.addItem(DISABLE_USER_OPTION, "Disable User", new DisableUserAction());
            menu.addItem(ENABLE_USER_OPTION, "Enable User", new EnableUserAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildCustomersMenu() {
        final Menu menu = new Menu("Customers >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(REGISTER_CUSTOMER_OPTION, "Register Customer", new RegisterCustomerAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildShowRequestsMenu() {
        final Menu menu = new Menu("Show Request >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(REGISTER_SHOW_REQUEST_OPTION, "Register Show Request", new RegisterShowRequestAction());
            menu.addItem(EDIT_SHOW_RESPONSE_OPTION, "Edit Show Request", new EditShowRequestAction());
            menu.addItem(LIST_SHOW_REQUESTS_CUSTOMER_OPTION1, "List Show Requests Customer", new ListShowRequestCustomerAction());
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.CRM_MANAGER)) {
            menu.addItem(LIST_SHOW_REQUESTS_CUSTOMER_OPTION2, "List Show Requests Customer", new ListShowRequestCustomerAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildCatalogueMenu() {
        final Menu menu = new Menu("Catalogue >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(LIST_PUBLIC_FIGURES_OPTION, "List Public Figures in Catalogue", new ListAllPublicFiguresCatalogueAction());
            menu.addItem(SEARCH_BY_KEYWORD_AND_CATEGORY_OPTION, "Search Figure by Keyword and Category", new SearchFigureByCategoryAndKeywordAction());
            menu.addItem(SEARCH_BY_KEYWORD_OPTION, "Search Figure by Keyword", new SearchFigureByKeywordAction());
            menu.addItem(SEARCH_BY_CATEGORY_OPTION, "Search Figure by Category", new SearchFigureByCategoryAction());
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER)) {
            menu.addItem(ADD_FIGURE_OPTION, "Add Figure to Catalogue", new AddFigureCatalogueAction());
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER)) {
            menu.addItem(DECOMMISSION_FIGURE_OPTION, "Decommission Figure from Catalogue", new DecommissionFigureCatalogueAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildFigureCategoryMenu() {
        final Menu menu = new Menu("Figure Categories >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER)) {
            menu.addItem(ADD_FIGURE_CATEGORY_OPTION, "Add Figure Category", new AddFigureCategoryAction());
            menu.addItem(EDIT_FIGURE_CATEGORY_OPTION, "Edit Figure Category", new ChangeFigureCategoryAction());
            menu.addItem(LIST_FIGURE_CATEGORY_OPTION, "List Figure Categories", new ListFigureCategoryAction());
            menu.addItem(ACTIVATE_FIGURE_CATEGORY_OPTION, "Activate Figure Category", new ActivateFigureCategoryAction());
            menu.addItem(INACTIVATE_FIGURE_CATEGORY_OPTION, "Inactivate Figure Category", new InactivateFigureCategoryAction());
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(LIST_FIGURE_CATEGORY_OPTION, "List Figure Categories", new ListFigureCategoryAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;
    }

    private Menu buildShowProposalsMenu() {
        final Menu menu = new Menu("Show Proposal >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR)) {
            menu.addItem(CREATE_SHOW_PROPOSAL_OPTION, "Create Show Proposal", new CreateShowProposalAction());
            menu.addItem(ADD_DRONE_PROPOSAL_OPTION, "Add Drone Proposal", new AddDroneProposalAction());
            menu.addItem(ADD_FIGURE_PROPOSAL_OPTION, "Add Figure Proposal", new AddFigureProposalAction());
            menu.addItem(ADD_VIDEO_PROPOSAL_OPTION, "Add Video Simulation Proposal", new AddVideoSimulationProposalAction());
            menu.addItem(SEND_PROPOSAL_OPTION, "Send Show Proposal", new SendProposalAction());
        }


        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildIntegrationsMenu() {
        final Menu menu = new Menu("Integrations >");

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH)) {
            menu.addItem(REGISTER_DSL_PLUGIN, "Register DSL Plugin", new RegisterDSLPluginAction());
            menu.addItem(REGISTER_DRONE_LANGUAGE_PLUGIN, "Register Drone Language Plugin", new RegisterDroneLanguagePluginAction());
            menu.addItem(IMPORT_DSL, "Validate DSL Description", new ValidateDSLDescriptionAction());
            menu.addItem(IMPORT_DRONE_DSL, "Validate Drone Language", new ValidateDroneLanguageAction());
        }

        if (authz.isAuthenticatedUserAuthorizedTo(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER)) {
            menu.addItem(REGISTER_PROPOSAL_TEMPLATE, "Register Proposal Template", new RegisterProposalTemplateAction());
            menu.addItem(IMPORT_PROPOSAL_TEMPLATES, "Validate Proposal Document", new ValidateProposalDocumentAction());
        }

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }
}
