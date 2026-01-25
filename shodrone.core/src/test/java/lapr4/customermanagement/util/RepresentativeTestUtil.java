package lapr4.customermanagement.util;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import lapr4.customermanagement.domain.*;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.util.ShodroneUserTestUtil;

public final class RepresentativeTestUtil {

    public static final String DEFAULT_EMAIL = "rep@company.com";
    public static final String DEFAULT_POSITION = "Representative";

    private RepresentativeTestUtil() {
    }

    public static Representative dummyRepresentative() {
        return dummyRepresentative(EmailAddress.valueOf(DEFAULT_EMAIL));
    }

    public static Representative dummyRepresentative(EmailAddress email) {
        return new Representative(email, CustomerTestUtil.dummyCustomer(),
                ShodroneUserTestUtil.dummyShodroneUser(),
                Designation.valueOf(DEFAULT_POSITION));
    }

    public static Representative dummyRepresentative(String email, Customer customer,
                                                     ShodroneUser user, String position) {
        return new Representative(EmailAddress.valueOf(email), customer, user, Designation.valueOf(position));
    }

}