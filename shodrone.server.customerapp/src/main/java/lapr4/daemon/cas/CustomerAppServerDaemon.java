package lapr4.daemon.cas;

import lapr4.ShodroneBaseApplication;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.showproposalmanagement.infrastructure.HandlerFactory;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lapr4.daemon.cas.presentation.CsvCustomerAppProtocolServer;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.customerapp.csvprotocol.server.CsvCustomerAppProtocolMessageParser;
import lapr4.usermanagement.domain.ShodronePasswordPolicy;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;

/**
 * App Customer Server daemon. Check the Customer application for a demo client of
 * this server application.
 *
 */

@SuppressWarnings("squid:S106")
public final class CustomerAppServerDaemon extends ShodroneBaseApplication {

    private static final int CAS_PORT = 8080;

    private static final Logger LOGGER = LogManager.getLogger(CustomerAppServerDaemon.class);

    /**
     * avoid instantiation of this class.
     */
    private CustomerAppServerDaemon() {
        super();
    }

    public static void main(final String[] args) {

        new CustomerAppServerDaemon().run(args);
    }

    @Override
    protected void doMain(final String[] args) {
        System.out.println("Starting the Customer App Server daemon...");
        LOGGER.info("Configuring the daemon...");

        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
                new PlainTextEncoder());


        LOGGER.info("Starting the server socket on port {}...\n", CAS_PORT);
        final var server = new CsvCustomerAppProtocolServer(buildServerDependencies());
        server.start(CAS_PORT, true);

        LOGGER.info("Exiting the daemon...");
        System.exit(0);
    }

    @Override
    protected String appTitle() {
        return "Server Shodrone ";
    }

    @Override
    protected String appGoodbye() {
        return "Server done.";
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

    private static CsvCustomerAppProtocolMessageParser buildServerDependencies() {
        return new CsvCustomerAppProtocolMessageParser(AuthzRegistry.authenticationService());
    }

}

