package lapr4.bootstrapers;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.infrastructure.authz.application.AuthenticationService;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.strings.util.Strings;
import eapli.framework.validations.Invariants;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.UserBuilderHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("squid:S106")
public class ShodroneBootstrapper implements Action {
	private static final Logger LOGGER = LogManager.getLogger(ShodroneBootstrapper.class);

	private static final String POWERUSER_PWD = "PoweruserA1";
	private static final String POWERUSER = "poweruser@email.org";

	private final AuthorizationService authz = AuthzRegistry.authorizationService();
	private final AuthenticationService authenticationService = AuthzRegistry.authenticationService();
	private final UserRepository userRepository = PersistenceContext.repositories().users();

	@Override
	public boolean execute() {
		// declare bootstrap actions
		final Action[] actions = { new MasterUsersBootstrapper() };

		registerPowerUser(userRepository);
		authenticateForBootstrapping();

		// execute all bootstrapping
		var ret = true;
		for (final Action boot : actions) {
			System.out.println("Bootstrapping " + nameOfEntity(boot) + "...");
			ret &= boot.execute();
		}
		return ret;
	}

	public static boolean registerPowerUser(final UserRepository userRepository) {
		final var userBuilder = UserBuilderHelper.builder();
		userBuilder.withEmailAsUsername(POWERUSER).withPassword(POWERUSER_PWD)
				.withName("Joe", "Power").withRoles(ShodroneRoles.POWER_USER);
		final var newUser = userBuilder.build();

		try {
			final var poweruser = userRepository.save(newUser);
			assert poweruser != null;
			return true;
		} catch (ConcurrencyException | IntegrityViolationException e) {
			// ignoring exception. assuming it is just a primary key violation
			// due to the tentative of inserting a duplicated user
			LOGGER.warn("Assuming {} already exists (activate trace log for details)", newUser.username());
			LOGGER.trace("Assuming existing record", e);
			return false;
		}
	}

	/**
	 * authenticate a super user to be able to register new users
	 *
	 */
	protected void authenticateForBootstrapping() {
		authenticationService.authenticate(POWERUSER, POWERUSER_PWD);
		Invariants.ensure(authz.hasSession());
	}

	private String nameOfEntity(final Action boot) {
		final var name = boot.getClass().getSimpleName();
		return Strings.left(name, name.length() - "Bootstrapper".length());
	}

}
