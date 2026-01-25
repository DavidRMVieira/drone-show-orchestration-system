package lapr4.customermanagement.util;

import eapli.framework.infrastructure.authz.domain.model.Name;
import lapr4.customermanagement.domain.*;

public final class CustomerTestUtil {

    public static final String DEFAULT_VAT = "PT123456789";
    public static final String DEFAULT_FIRST_NAME = "Test";
    public static final String DEFAULT_LAST_NAME = "Customer";
    public static final String DEFAULT_STREET = "Main Street";
    public static final String DEFAULT_CITY = "Lisbon";
    public static final String DEFAULT_ZIPCODE = "1000-001";
    public static final String DEFAULT_COUNTRY = "Portugal";

    public static Customer dummyCustomer() {
        VAT vat = VAT.valueOf(DEFAULT_VAT);
        Name name = Name.valueOf(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME);
        Address address = new Address(DEFAULT_STREET, DEFAULT_CITY, DEFAULT_ZIPCODE, DEFAULT_COUNTRY);
        return new Customer(vat, name, address, CustomerState.CREATED, CustomerType.REGULAR);
    }

    public static Customer dummyCustomer(VAT vat) {
        Name name = Name.valueOf(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME);
        Address address = new Address(DEFAULT_STREET, DEFAULT_CITY, DEFAULT_ZIPCODE, DEFAULT_COUNTRY);
        return new Customer(vat, name, address, CustomerState.CREATED, CustomerType.REGULAR);
    }

    public static Customer dummyCustomer(String vat, String firstName, String lastName, String street, String city,
                                         String postalCode, String country, CustomerState state, CustomerType type) {
        return new Customer(VAT.valueOf(vat), Name.valueOf(firstName, lastName),
                Address.valueOf(street, city, postalCode, country), state, type);
    }

}