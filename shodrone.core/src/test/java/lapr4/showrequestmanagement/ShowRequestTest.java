package lapr4.showrequestmanagement;

import lapr4.customermanagement.domain.*;
import lapr4.customermanagement.util.CustomerTestUtil;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.Place;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.domain.ShowRequestState;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ShowRequestTest {

    private static final Place VALID_PLACE = new Place("Auditorium A");
    private static final Date VALID_DATE = new Date();
    private static final Duration VALID_DURATION = new Duration(120);
    private static final ShowRequestState VALID_STATE = ShowRequestState.PENDING;
    private static final Customer VALID_CUSTOMER = CustomerTestUtil.dummyCustomer("AA111111111", "Igor", "Coutinho", "Rua Porto", "Porto", "4444-111", "Portugal", CustomerState.CREATED, CustomerType.REGULAR);

    private static final Place NEW_PLACE = new Place("Conference Room B");
    private static final Date NEW_DATE = new Date(System.currentTimeMillis() + 86400000); // Tomorrow
    private static final Duration NEW_DURATION = new Duration(180);

    @Test
    void ensureValidShowRequestIsCreated() {
        final var showRequest = new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, VALID_STATE, VALID_CUSTOMER);
        assertNotNull(showRequest);
        assertEquals(VALID_PLACE.toString(), showRequest.toDTO().getPlace().toString());
        assertEquals(VALID_DATE.toString(), showRequest.toDTO().getDate().toString());
        assertEquals(VALID_DURATION.toString(), showRequest.toDTO().getDuration().toString());
        assertEquals(VALID_STATE.toString(), showRequest.toDTO().getState().toString());
        assertEquals(VALID_CUSTOMER.identity().toString(), showRequest.toDTO().getCustomer().toString());
    }

    @Test
    void ensureAllParametersAreRequired() {
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(null, VALID_DATE, VALID_DURATION, VALID_STATE, VALID_CUSTOMER));
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(VALID_PLACE, null, VALID_DURATION, VALID_STATE, VALID_CUSTOMER));
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(VALID_PLACE, VALID_DATE, null, VALID_STATE, VALID_CUSTOMER));
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, null, VALID_CUSTOMER));
        assertThrows(IllegalArgumentException.class, () -> new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, VALID_STATE, null));
    }

    @Test
    void ensurePlaceCanBeChanged() {
        final var showRequest = new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, VALID_STATE, VALID_CUSTOMER);
        showRequest.changePlaceTo(NEW_PLACE);
        assertEquals(NEW_PLACE.toString(), showRequest.toDTO().getPlace().toString());
    }

    @Test
    void ensureDateCanBeChanged() {
        final var showRequest = new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, VALID_STATE, VALID_CUSTOMER);
        showRequest.changeDateTo(NEW_DATE);
        assertEquals(NEW_DATE.toString(), showRequest.toDTO().getDate().toString());
    }

    @Test
    void ensureDurationCanBeChanged() {
        final var showRequest = new ShowRequest(VALID_PLACE, VALID_DATE, VALID_DURATION, VALID_STATE, VALID_CUSTOMER);
        showRequest.changeDurationTo(NEW_DURATION);
        assertEquals(NEW_DURATION.toString(), showRequest.toDTO().getDuration().toString());
    }
}