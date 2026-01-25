package lapr4.customermanagement.domain;

import lapr4.customermanagement.util.CustomerTestUtil;
import eapli.framework.infrastructure.authz.domain.model.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private final VAT vat1 = VAT.valueOf("PT111111111");
    private final VAT vat2 = VAT.valueOf("PT222222222");
    private final Name name = Name.valueOf("Test", "Customer");
    private final Address address = new Address("Main St", "Lisbon", "1000-001", "Portugal");
    private final CustomerState state = CustomerState.CREATED;
    private final CustomerType type = CustomerType.REGULAR;

    @Test
    void ensureFailsIfAnyArgumentIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(null, name, address, state, type));
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(vat1, null, address, state, type));
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(vat1, name, null, state, type));
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(vat1, name, address, null, type));
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(vat1, name, address, state, null));
    }

    @Test
    void ensureIdentityReturnsVat() {
        final var customer = CustomerTestUtil.dummyCustomer(vat1);

        assertEquals(vat1, customer.identity());
    }

    @Test
    void ensureCustomerEqualsPassesForSameVat() {
        final var cust1 = CustomerTestUtil.dummyCustomer(vat1);
        final var cust2 = CustomerTestUtil.dummyCustomer(vat1);

        assertEquals(cust1, cust2);
    }

    @Test
    void ensureCustomerEqualsFailsForDifferentVats() {
        final var cust1 = CustomerTestUtil.dummyCustomer(vat1);
        final var cust2 = CustomerTestUtil.dummyCustomer(vat2);

        assertNotEquals(cust1, cust2);
    }

    @Test
    void ensureSameAsIsTrueForSameInstance() {
        final var cust = CustomerTestUtil.dummyCustomer(vat1);

        assertTrue(cust.sameAs(cust));
    }

    @Test
    void ensureSameAsFailsForDifferentVats() {
        final var cust1 = CustomerTestUtil.dummyCustomer(vat1);
        final var cust2 = CustomerTestUtil.dummyCustomer(vat2);

        assertFalse(cust1.sameAs(cust2));
    }

    @Test
    void ensureValidCustomerIsCreated() {
        final var customer = new Customer(vat1, name, address, state, type);
        assertNotNull(customer);
        assertEquals(vat1.toString(), customer.toDTO().getVatNumber().toString());
        assertEquals(name.toString(), customer.toDTO().getName().toString());
        assertEquals(address.toString(), customer.toDTO().getAddress().toString());
        assertEquals(state.toString(), customer.toDTO().getState().toString());
        assertEquals(type.toString(), customer.toDTO().getType().toString());
    }

}