package lapr4;

import eapli.framework.collections.util.ArrayPredicates;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import eapli.framework.io.util.Console;
import lapr4.bootstrapers.ShodroneBootstrapper;
import lapr4.bootstrapers.demo.ShodroneDemoBootstrapper;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodronePasswordPolicy;

@SuppressWarnings("squid:S106")
public final class ShodroneBootstrap extends ShodroneBaseApplication {

    private boolean isToBootstrapDemoData;
    private boolean isToRunSampleE2E;
    private boolean isToWaitInTheEnd;

    /**
     * avoid instantiation of this class.
     */
    private ShodroneBootstrap() {
        super();
    }

    public static void main(final String[] args) {

        new ShodroneBootstrap().run(args);
    }

    @Override
    protected void doMain(final String[] args) {
        handleArgs(args);

        System.out.println("\n\n------- MASTER DATA -------");
        new ShodroneBootstrapper().execute();

        if (isToBootstrapDemoData) {
            System.out.println("\n\n------- DEMO DATA -------");
            new ShodroneDemoBootstrapper().execute();
        }

        if (isToRunSampleE2E) {
//            System.out.println("\n\n------- BASIC SCENARIO -------");
//            new ShodroneDemoSmokeTester().execute();
        }

        if (isToWaitInTheEnd) {
            Console.readLine("\n\n>>>>>> Enter to finish the program.");
        }
    }

    private void handleArgs(final String[] args) {
        isToRunSampleE2E = ArrayPredicates.contains(args, "-smoke:e2e");
        if (isToRunSampleE2E) {
            isToBootstrapDemoData = true;
        } else {
            isToBootstrapDemoData = ArrayPredicates.contains(args, "-bootstrap:demo");
        }

        isToWaitInTheEnd = ArrayPredicates.contains(args, "-wait");
    }

    @Override
    protected String appTitle() {
        return "Bootstrapping Shodrone data ";
    }

    @Override
    protected String appGoodbye() {
        return "Bootstrap data done.";
    }

    @Override
    protected void configureAuthz() {
        AuthzRegistry.configure(PersistenceContext.repositories().users(), new ShodronePasswordPolicy(),
                new PlainTextEncoder());
    }

    @Override
    protected void doSetupEventHandlers(final EventDispatcher dispatcher) {
    }

}
