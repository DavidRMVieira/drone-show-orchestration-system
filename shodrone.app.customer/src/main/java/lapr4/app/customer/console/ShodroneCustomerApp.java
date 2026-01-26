package lapr4.app.customer.console;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import lapr4.ShodroneBaseApplication;
import lapr4.app.customer.console.authz.CredentialStore;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.app.customer.console.presentation.MainMenu;
import lapr4.presentation.authorization.LoginUI;
import lapr4.showproposal.application.AnalyzeProposalProxyController;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
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

import java.io.IOException;

import static lapr4.usermanagement.domain.ShodroneRoles.REPRESENTATIVE;

/**
 * Shodrone Customer Application. The client of the Show Proposal Daemon.
 *
 */
@SuppressWarnings("squid:S106")
public final class ShodroneCustomerApp extends ShodroneBaseApplication {

	private static final Logger LOGGER = LogManager.getLogger(ShodroneCustomerApp.class);

	private ShodroneCustomerApp() {
        super();
    }

	public static void main(final String[] args) {

		new ShodroneCustomerApp().run(args);
	}

	@Override
	protected void doMain(final String[] args) {
		System.out.println("Welcome to the Shodrone Customer App!");
		System.out.println("This application allows you to manage your show proposals.");

		System.out.println("\n\nConnecting to the Customer App Server...");
		smokeTestCustomerAppServer();

		final var authenticated = new LoginUI(CredentialStore.STORE_CREDENTIALS, REPRESENTATIVE).show();
		if (authenticated) {
			// go to the main menu
			final var menu = new MainMenu();
			menu.mainLoop();
		} else {
			System.out.println("You must login to use the application.");
		}

	}

	private void smokeTestCustomerAppServer() {

		try {
			// simulate login - customer representative with a show proposal that is awaiting response
			CredentialStore.STORE_CREDENTIALS.authenticated("pedro.shodrone@showdrone.com", "Password1", REPRESENTATIVE);

			final var analyzeController = new AnalyzeProposalProxyController();

			analyzeController.listRepresentativeShowProposalsAwaitingResponse(CredentialStore.getUsername(),
					CredentialStore.getPassword());

			System.out.println("\nConnection established!");

//			// simulate another login - customer representative with a show proposal that is scheduled
//			CredentialStore.STORE_CREDENTIALS.authenticated("jane.shodrone@showdrone.com", "Password1", REPRESENTATIVE);
//
//			final var scheduledController = new ListScheduledShowsProxyController();
//
//			scheduledController.listCustomerScheduledShows(CredentialStore.getUsername(),
//					CredentialStore.getPassword());

		} catch (final IOException e) {
			System.out.println("Problems with network connection: " + e.getMessage());
			LOGGER.debug(e);
			System.exit(0);
		} catch (final FailedRequestException e) {
			System.out.println("\nConnection established!");
			System.out.println("Problems with requests: " + e.getMessage());
		}
	}

	@Override
	protected String appTitle() {
		return "Shodrone Customer App";
	}

	@Override
	protected String appGoodbye() {
		return "Thank you for using 'Shodrone Customer App'. \nGoodbye!";
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
