package lapr4.app.customer.console.authz;

import lapr4.infrastructure.CredentialHandler;
import lombok.Getter;

import static lapr4.usermanagement.domain.ShodroneRoles.POWER_USER;
import static lapr4.usermanagement.domain.ShodroneRoles.REPRESENTATIVE;

/**
 * Credential store to hold in memory the username and password collected during
 * login.
 *
 */
public class CredentialStore {

	@Getter
    private static String username;
	@Getter
    private static String password;

    public static final CredentialHandler STORE_CREDENTIALS = (u, p, r) -> {
		CredentialStore.username = u;
		CredentialStore.password = p;
		return (r.equals(POWER_USER) || r.equals(REPRESENTATIVE));
	};
}
