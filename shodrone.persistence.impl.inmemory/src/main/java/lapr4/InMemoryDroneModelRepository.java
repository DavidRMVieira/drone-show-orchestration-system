package lapr4;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.repositories.DroneModelRepository;

public class InMemoryDroneModelRepository extends InMemoryDomainRepository<DroneModel, Designation>
        implements DroneModelRepository {

    static {
        InMemoryInitializer.init();
    }

}
