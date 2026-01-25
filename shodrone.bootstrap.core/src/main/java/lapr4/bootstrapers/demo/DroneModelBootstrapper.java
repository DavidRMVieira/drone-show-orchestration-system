package lapr4.bootstrapers.demo;

import eapli.framework.actions.Action;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import lapr4.bootstrapers.TestDataConstants;
import lapr4.droneinventory.apllication.RegisterDroneModelController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DroneModelBootstrapper implements Action {
    private static final Logger LOGGER = LogManager.getLogger(DroneModelBootstrapper.class);

    private final RegisterDroneModelController controller = new RegisterDroneModelController();

    @Override
    public boolean execute() {
        try {
            registerDroneModel(TestDataConstants.DRONE_MODEL_NAME_1, "Skydio");
            registerDroneModel(TestDataConstants.DRONE_MODEL_NAME_2, "Parrot");
            return true;
        } catch (Exception ex) {
            LOGGER.warn("Error while bootstrapping drone models", ex);
            return false;
        }
    }

    private void registerDroneModel(String name, String manufacturerName) {
        try {
            controller.registerDroneModel(name, manufacturerName);

        } catch (IntegrityViolationException | ConcurrencyException e) {
            LOGGER.trace("Assuming existing record", e);
        }
    }

}
