package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.droneinventory.domain.Drone;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.domain.DroneSerialNumber;
import lapr4.droneinventory.domain.DroneState;
import lapr4.droneinventory.repositories.DroneRepository;

import java.util.stream.StreamSupport;

public class InMemoryDroneRepository extends InMemoryDomainRepository<Drone, DroneSerialNumber>
        implements DroneRepository {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public long countByModelAndState(DroneModel model, DroneState state) {
        return StreamSupport.stream(match(e -> e.state().equals(state) && e.model().equals(model)).spliterator(), false)
                .count();
    }

}
