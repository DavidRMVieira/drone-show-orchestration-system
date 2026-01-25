package lapr4.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.framework.util.Utility;

@Utility
public class UserBuilderHelper {
	private UserBuilderHelper() {
		// ensure utility
	}

	public static SystemUserBuilder builder() {
		return new SystemUserBuilder(new ShodronePasswordPolicy(), new PlainTextEncoder());
	}

}
