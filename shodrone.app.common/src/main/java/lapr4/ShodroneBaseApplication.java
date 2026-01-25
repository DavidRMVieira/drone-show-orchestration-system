package lapr4;

import eapli.framework.infrastructure.pubsub.EventDispatcher;
import eapli.framework.infrastructure.pubsub.PubSubRegistry;
import eapli.framework.infrastructure.pubsub.impl.inprocess.service.InProcessPubSub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("squid:S106")
public abstract class ShodroneBaseApplication {

	protected static final String SEPARATOR_HR = "============================================";
	private static final Logger LOGGER = LogManager.getLogger(ShodroneBaseApplication.class);

    /**
	 * @param args the command line arguments
	 */
	public void run(final String[] args) {
		configure();

		printHeader();

		try {
			setupEventHandlers();

			doMain(args);

			printFooter();

//		} catch (final Exception e) {
//			System.out.println(
//					"Something unexpected has happened and the application will terminate. Please check the logs.\n");
//			LOGGER.error(e);

		} finally {
			clearEventHandlers();
		}

		// exiting the application, closing all threads
		System.exit(0);
	}

	protected void printFooter() {
		System.out.println("\n");
		System.out.println(SEPARATOR_HR);
		System.out.println(appGoodbye());
		System.out.println(SEPARATOR_HR);
	}

	protected void printHeader() {
		System.out.println(SEPARATOR_HR);
		System.out.println(appTitle() + " " + Application.VERSION);
		System.out.println(Application.COPYRIGHT);
		System.out.println(SEPARATOR_HR);
	}

	private final void clearEventHandlers() {
		try {
			doClearEventHandlers(PubSubRegistry.dispatcher());

			PubSubRegistry.dispatcher().shutdown();
		} catch (final Exception e) {
			LOGGER.error("Unable to cleanup event handlers", e);
		}
	}

	private final void setupEventHandlers() {
		doSetupEventHandlers(PubSubRegistry.dispatcher());
	}

	protected void configure() {
		configureAuthz();

		configurePubSub();
	}

	protected void configurePubSub() {
		// TODO use a factory/registry to obtain the pub/sub engine
		/*
		 * SimplePersistentPubSub.configure(PersistenceContext.repositories().
		 * eventRecord(), PersistenceContext.repositories().eventConsumption(),
		 * Application.settings().getProperty("eapli.framework.pubsub.instanceKey"),
		 * Integer.valueOf(Application.settings().getProperty(
		 * "eapli.framework.pubsub.poolInterval") ));
		 * PubSubRegistry.configure(SimplePersistentPubSub.dispatcher(),
		 * SimplePersistentPubSub.publisher());
		 */
		PubSubRegistry.configure(InProcessPubSub.dispatcher(), InProcessPubSub.publisher());
	}

	protected abstract void configureAuthz();

	protected abstract void doMain(final String[] args);

	protected abstract String appTitle();

	protected abstract String appGoodbye();

	protected void doClearEventHandlers(final EventDispatcher dispatcher) {
		// nothing to do
	}

	protected abstract void doSetupEventHandlers(EventDispatcher dispatcher);
}
