package lapr4.app.testing.console;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import lapr4.ShodroneBaseApplication;
import lapr4.infrastructure.AuthenticationCredentialHandler;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.app.testing.console.presentation.MainMenu;
import lapr4.presentation.authorization.LoginUI;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.showproposalmanagement.infrastructure.HandlerFactory;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodronePasswordPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static lapr4.usermanagement.domain.ShodroneRoles.DRONE_TECH;

/**
 * Shodrone Testing Application. The client of the Simulator, responsible for sending a selected show to the drone tech for testing.
 *
 */
@SuppressWarnings("squid:S106")
public final class ShodroneTestingApp extends ShodroneBaseApplication {

	private static final Logger LOGGER = LogManager.getLogger(ShodroneTestingApp.class);

	private ShodroneTestingApp() {
        super();
    }

	public static void main(final String[] args) {

		new ShodroneTestingApp().run(args);
	}

	@Override
	protected void doMain(final String[] args) {
		System.out.println("Welcome to the Shodrone Testing App!");
		System.out.println("This application allows you to testing your show proposals.");

		System.out.println("\n\nConnecting to the Simulator...");

		// smoke test ............ TO IMPLEMENT

		System.out.println("\nConnection established!");

		// login and go to main menu
		final boolean authenticated = new LoginUI(new AuthenticationCredentialHandler(), DRONE_TECH).show();
		if (authenticated) {
			// go to main menu
			final var menu = new MainMenu();
			menu.mainLoop();
		} else {
			System.out.println("You must login to use the application.");
		}

	}


	@Override
	protected String appTitle() {
		return "Shodrone Testing App";
	}

	@Override
	protected String appGoodbye() {
		return "Thank you for using 'Shodrone Testing App'. \nGoodbye!";
	}

	@Override
	protected void configureAuthz() {
		AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
				new PlainTextEncoder());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetupEventHandlers(final EventDispatcher dispatcher) {
		final ShowProposalRepository repo = PersistenceContext.repositories().showProposals();
		final ListShowProposalService svc = new ListShowProposalService();
		final HandlerFactory factory = new HandlerFactory(repo, svc);

		dispatcher.subscribe(factory.createSentHandler(), ShowProposalSentEvent.class);
		dispatcher.subscribe(factory.createAcceptedHandler(), ShowProposalAcceptedEvent.class);
		dispatcher.subscribe(factory.createRejectedHandler(), ShowProposalRejectedEvent.class);
		dispatcher.subscribe(factory.createAcceptedByCollaboratorHandler(), ShowProposalAcceptedByCRMEvent.class);
	}
}
