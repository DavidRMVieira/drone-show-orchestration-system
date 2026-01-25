package lapr4;

import eapli.framework.general.domain.model.Designation;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.repositories.DroneModelRepository;

public class JpaDroneModelRepository extends ShodroneJpaRepositoryBase<DroneModel, Long, Designation>
        implements DroneModelRepository {

    public JpaDroneModelRepository() {
        super("modelName");
    }

}
