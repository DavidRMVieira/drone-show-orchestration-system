package lapr4;

import lapr4.droneinventory.domain.Drone;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.domain.DroneSerialNumber;
import lapr4.droneinventory.domain.DroneState;
import lapr4.droneinventory.repositories.DroneRepository;

public class JpaDroneRepository extends ShodroneJpaRepositoryBase<Drone, Long, DroneSerialNumber>
        implements DroneRepository {

    public JpaDroneRepository() {
        super("serialNumber");
    }

    @Override
    public long countByModelAndState(DroneModel model, DroneState state) {
        final var query = entityManager().createQuery(
                "SELECT COUNT(d) FROM Drone d WHERE d.model = :model AND d.state = :state"
        );
        query.setParameter("model", model);
        query.setParameter("state", state);
        return (long) query.getSingleResult();
    }

}
