package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.droneinventory.apllication.AddDroneInventoryController;
import lapr4.droneinventory.dto.DroneModelDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class DroneBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(DroneBootstrapper.class);

    private final AddDroneInventoryController controller = new AddDroneInventoryController();

    @Override
    public boolean execute() {
        try {
            registerDrone("ANAFI-FR-87530219", createDate(2025, Calendar.JANUARY, 15), new DroneModelDTO(TestDataConstants.DRONE_MODEL_NAME_1, "Skydio"));
            registerDrone("YUN-HPLUS", createDate(2025, Calendar.FEBRUARY, 1), new DroneModelDTO(TestDataConstants.DRONE_MODEL_NAME_1, "Skydio"));
            registerDrone("DJI-P4P-2024", createDate(2024, Calendar.JUNE, 19), new DroneModelDTO(TestDataConstants.DRONE_MODEL_NAME_2, "Parrot"));
            return true;
        } catch (Exception ex) {
            LOGGER.warn("Error while bootstrapping drones", ex);
            return false;
        }
    }

    private void registerDrone(String serialNumber, Date acquisitionDate, DroneModelDTO modelDTO) {
        try {
            controller.addDroneInventory(serialNumber, acquisitionDate, modelDTO);

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
