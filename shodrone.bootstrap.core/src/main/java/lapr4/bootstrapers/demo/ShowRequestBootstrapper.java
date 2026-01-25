package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.showrequestmanagement.application.RegisterShowRequestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class ShowRequestBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(ShowRequestBootstrapper.class);

    private final RegisterShowRequestController controller = new RegisterShowRequestController();

    @Override
    public boolean execute() {
        try {
            registerShowRequest("THEATER", createDate(2025, Calendar.JUNE, 15), 120, new CustomerDTO(TestDataConstants.CUSTOMER_VAT_1, null, null, null, null));
            registerShowRequest("CONCERT_HALL", createDate(2025, Calendar.JULY, 10), 90, new CustomerDTO(TestDataConstants.CUSTOMER_VAT_2, null, null, null, null));
            registerShowRequest("OPEN_AIR", createDate(2025, Calendar.AUGUST, 5), 60, new CustomerDTO(TestDataConstants.CUSTOMER_VAT_3, null, null, null, null));
            return true;
        } catch (Exception ex) {
            LOGGER.warn("Error while bootstrapping show requests", ex);
            return false;
        }
    }

    private void registerShowRequest(final String place, final Date date, final int duration, final CustomerDTO customer) {
        try {
            controller.registerShowRequest(place, date, duration, customer);

        } catch (IntegrityViolationException | ConcurrencyException e) {
            LOGGER.trace("Assuming existing record", e);
        }
    }

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 20, 0, 0); // 20:00h as example
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
