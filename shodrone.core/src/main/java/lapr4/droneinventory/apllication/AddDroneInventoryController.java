package lapr4.droneinventory.apllication;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.droneinventory.domain.Drone;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.domain.DroneSerialNumber;
import lapr4.droneinventory.domain.DroneState;
import lapr4.droneinventory.dto.DroneDTO;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.droneinventory.repositories.DroneRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.Date;

@UseCaseController
public class AddDroneInventoryController {

    private final DroneRepository droneRepo;
    private final ListDroneModelService modelSvc;
    private final AuthorizationService authz;

    public AddDroneInventoryController() {
        this.droneRepo = PersistenceContext.repositories().drones();
        this.modelSvc = new ListDroneModelService();
        this.authz = AuthzRegistry.authorizationService();
    }

    public DroneDTO addDroneInventory(String serialNumber, Date acquisitionDate, DroneModelDTO modelDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);

        DroneModel model = modelSvc.findDroneModelByName(modelDTO.getName());
        Drone drone = new Drone(DroneSerialNumber.valueOf(serialNumber), acquisitionDate, DroneState.ACTIVE, model);

        return droneRepo.save(drone).toDTO();
    }

    public Iterable<DroneModelDTO> listModels() {
        return modelSvc.allDroneModels();
    }

}
