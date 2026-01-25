package lapr4.usermanagement.util;

import eapli.framework.infrastructure.authz.domain.model.*;
import lapr4.usermanagement.domain.*;

public final class ShodroneUserTestUtil {

    public static final String DEFAULT_EMAIL = "dummy@showdrone.com";
    public static final String DEFAULT_PHONE = "910000000";
    public static final String DEFAULT_PASSWORD = "DumMy1!";
    public static final String DEFAULT_FIRSTNAME = "Dummy";
    public static final String DEFAULT_LASTNAME = "User";
    public static final Role DEFAULT_ROLE = Role.valueOf("ADMIN");

    private ShodroneUserTestUtil() {
    }

    public static SystemUser newDummyUser() {
        return dummyUser(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_ROLE);
    }

    public static SystemUser dummyUser(final String email, final String password, final String firstName, final String lastName, final Role... roles) {
        final var userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
        return userBuilder.withEmailAsUsername(email)
                .withPassword(password)
                .withName(firstName, lastName)
                .withRoles(roles)
                .build();
    }

    public static ShodroneUser dummyShodroneUser() {
        return dummyShodroneUser(new ShodroneEmail(DEFAULT_EMAIL));
    }

    public static ShodroneUser dummyShodroneUser(final ShodroneEmail email) {
        return new ShodroneUser(newDummyUser(), email, PhoneNumber.valueOf(DEFAULT_PHONE));
    }

    public static ShodroneUser dummyShodroneUser(final ShodroneEmail email, final PhoneNumber phoneNumber) {
        return new ShodroneUser(newDummyUser(), email, phoneNumber);
    }

    public static ShodroneUser dummyShodroneUser(final ShodroneEmail email, final PhoneNumber phoneNumber, final SystemUser user) {
        return new ShodroneUser(user, email, phoneNumber);
    }

}
