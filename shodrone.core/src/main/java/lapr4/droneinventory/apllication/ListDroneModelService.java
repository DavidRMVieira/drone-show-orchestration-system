package lapr4.droneinventory.apllication;

import eapli.framework.application.ApplicationService;
import eapli.framework.general.domain.model.Designation;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.domain.DroneState;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.droneinventory.repositories.DroneModelRepository;
import lapr4.droneinventory.repositories.DroneRepository;
import lapr4.infrastructure.persistence.PersistenceContext;

@ApplicationService
public class ListDroneModelService {

    private final DroneModelRepository droneModelRepo = PersistenceContext.repositories().droneModels();
    private final DroneRepository droneRepo = PersistenceContext.repositories().drones();

    public Iterable<DroneModelDTO> allDroneModels() {
        final Iterable<DroneModel> droneModels = droneModelRepo.findAll();
        return DroneModelDTOParser.transformToDTO(droneModels);
    }

    public DroneModel findDroneModelByName(final String name) {
        return droneModelRepo.ofIdentity(Designation.valueOf(name))
                .orElseThrow(() -> new IllegalArgumentException("Unknown drone model: " + name));
    }

    public long countActiveDronesByModel(final DroneModel model) {
        return droneRepo.countByModelAndState(model, DroneState.ACTIVE);
    }

}
