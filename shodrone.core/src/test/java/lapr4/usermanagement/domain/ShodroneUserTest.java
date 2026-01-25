package lapr4.usermanagement.domain;

import lapr4.usermanagement.util.ShodroneUserTestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShodroneUserTest {

    private final ShodroneEmail email1 = new ShodroneEmail("user1@showdrone.com");
    private final ShodroneEmail email2 = new ShodroneEmail("user2@showdrone.com");
    private final PhoneNumber phone = PhoneNumber.valueOf("912345678");

    @Test
    void ensureFailsIfAnyArgumentIsNull() {
        final var systemUser = ShodroneUserTestUtil.newDummyUser();

        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(null, email1, phone));
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(systemUser, null, phone));
        assertThrows(IllegalArgumentException.class, () -> new ShodroneUser(systemUser, email1, null));
    }

    @Test
    void ensureIdentityReturnsEmail() {
        final var user = ShodroneUserTestUtil.dummyShodroneUser(email1);

        assertEquals(email1, user.identity());
    }

    @Test
    void ensureShodroneUserEqualsPassesForSameEmail() {
        final var user1 = ShodroneUserTestUtil.dummyShodroneUser(email1);
        final var user2 = ShodroneUserTestUtil.dummyShodroneUser(email1);

        assertEquals(user1, user2);
    }

    @Test
    void ensureShodroneUserEqualsFailsForDifferentEmails() {
        final var user1 = ShodroneUserTestUtil.dummyShodroneUser(email1);
        final var user2 = ShodroneUserTestUtil.dummyShodroneUser(email2);

        assertNotEquals(user1, user2);
    }

    @Test
    void ensureShodroneUserEqualsFailsForDifferentObjectType() {
        final var user = ShodroneUserTestUtil.dummyShodroneUser(email1);

        assertNotEquals(user, ShodroneUserTestUtil.newDummyUser());
    }

    @Test
    void ensureSameAsIsTrueForSameInstance() {
        final var user = ShodroneUserTestUtil.dummyShodroneUser(email1);

        assertTrue(user.sameAs(user));
    }

    @Test
    void ensureSameAsFailsForDifferentUsers() {
        final var user1 = ShodroneUserTestUtil.dummyShodroneUser(email1);
        final var user2 = ShodroneUserTestUtil.dummyShodroneUser(email2);

        assertFalse(user1.sameAs(user2));
    }

}
