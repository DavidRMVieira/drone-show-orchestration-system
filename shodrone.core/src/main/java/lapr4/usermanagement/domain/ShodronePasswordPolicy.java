package lapr4.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.PasswordPolicy;
import eapli.framework.strings.util.StringPredicates;

public class ShodronePasswordPolicy implements PasswordPolicy {

	@Override
	public boolean isSatisfiedBy(final String rawPassword) {

		// at least 6 characters long
		// at least one digit
		if (StringPredicates.isNullOrEmpty(rawPassword) || (rawPassword.length() < 6) || !StringPredicates.containsDigit(rawPassword)) {
			return false;
		}

		// at least one capital letter
		return StringPredicates.containsCapital(rawPassword);
	}

	public PasswordStrength strength(final String rawPassword) {
		PasswordStrength passwordStrength;
		if (rawPassword.length() >= 12
				|| (rawPassword.length() >= 8 && StringPredicates.containsAny(rawPassword, "$#!%&?"))) {
			passwordStrength = PasswordStrength.EXCELENT;
		} else if (rawPassword.length() >= 8) {
			passwordStrength = PasswordStrength.GOOD;
		} else {
			if (rawPassword.length() >= 6) {
			}
			passwordStrength = PasswordStrength.WEAK;
		}
		return passwordStrength;
	}

	public enum PasswordStrength {
		WEAK, GOOD, EXCELENT,
	}
}
