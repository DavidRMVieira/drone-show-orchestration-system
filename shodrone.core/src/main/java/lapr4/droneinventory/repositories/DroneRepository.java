package lapr4.droneinventory.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.droneinventory.domain.Drone;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.domain.DroneSerialNumber;
import lapr4.droneinventory.domain.DroneState;

public interface DroneRepository extends DomainRepository<DroneSerialNumber, Drone> {

    long countByModelAndState(DroneModel model, DroneState state);

}
