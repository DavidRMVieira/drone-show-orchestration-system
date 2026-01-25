package lapr4.customermanagement.domain;

import lapr4.customermanagement.util.CustomerTestUtil;
import lapr4.customermanagement.util.RepresentativeTestUtil;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.util.ShodroneUserTestUtil;
import org.junit.jupiter.api.Test;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;

import static org.junit.jupiter.api.Assertions.*;

class RepresentativeTest {

    private final EmailAddress email1 = EmailAddress.valueOf("rep1@company.com");
    private final EmailAddress email2 = EmailAddress.valueOf("rep2@company.com");
    private final Designation position = Designation.valueOf("Sales Manager");
    private final ShodroneUser user = ShodroneUserTestUtil.dummyShodroneUser();

    @Test
    void ensureFailsIfAnyArgumentIsNull() {
        final var customer = CustomerTestUtil.dummyCustomer();

        assertThrows(IllegalArgumentException.class,
                () -> new Representative(null, customer, user, position));
        assertThrows(IllegalArgumentException.class,
                () -> new Representative(email1, null, user, position));
        assertThrows(IllegalArgumentException.class,
                () -> new Representative(email1, customer, null, position));
        assertThrows(IllegalArgumentException.class,
                () -> new Representative(email1, customer, user, null));
    }

    @Test
    void ensureIdentityReturnsEmail() {
        final var representative = RepresentativeTestUtil.dummyRepresentative(email1);

        assertEquals(email1, representative.identity());
    }

    @Test
    void ensureRepresentativeEqualsPassesForSameEmail() {
        final var rep1 = RepresentativeTestUtil.dummyRepresentative(email1);
        final var rep2 = RepresentativeTestUtil.dummyRepresentative(email1);

        assertEquals(rep1, rep2);
    }

    @Test
    void ensureRepresentativeEqualsFailsForDifferentEmails() {
        final var rep1 = RepresentativeTestUtil.dummyRepresentative(email1);
        final var rep2 = RepresentativeTestUtil.dummyRepresentative(email2);

        assertNotEquals(rep1, rep2);
    }

    @Test
    void ensureSameAsIsTrueForSameInstance() {
        final var rep = RepresentativeTestUtil.dummyRepresentative(email1);

        assertTrue(rep.sameAs(rep));
    }

    @Test
    void ensureSameAsFailsForDifferentEmails() {
        final var rep1 = RepresentativeTestUtil.dummyRepresentative(email1);
        final var rep2 = RepresentativeTestUtil.dummyRepresentative(email2);

        assertFalse(rep1.sameAs(rep2));
    }

}