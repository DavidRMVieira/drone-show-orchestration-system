package lapr4;

import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import lapr4.infrastructure.AuthenticationCredentialHandler;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.presentation.authorization.LoginUI;
import lapr4.presentation.MainMenu;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.showproposalmanagement.infrastructure.HandlerFactory;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodronePasswordPolicy;

@SuppressWarnings("squid:S106")
public final class ShodroneBackoffice extends ShodroneBaseApplication {

    /**
     * avoid instantiation of this class.
     */
    private ShodroneBackoffice() {
        super();
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(final String[] args) {

        new ShodroneBackoffice().run(args);
    }

    @Override
    protected void doMain(final String[] args) {
        // login and go to main menu
        final boolean authenticated = new LoginUI(new AuthenticationCredentialHandler()).show();
        if (authenticated) {
            // go to main menu
            final var menu = new MainMenu();
            menu.mainLoop();
        }
    }

    @Override
    protected String appTitle() {
        return "Shodrone Back Office";
    }

    @Override
    protected String appGoodbye() {
        return "Shodrone Back Office";
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
