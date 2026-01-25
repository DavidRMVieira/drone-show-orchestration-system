package lapr4.droneinventory.apllication;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.droneinventory.repositories.DroneModelRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.Date;

@UseCaseController
public class RegisterDroneModelController {

    private final DroneModelRepository repo;
    private final AuthorizationService authz;

    public RegisterDroneModelController() {
        this.repo = PersistenceContext.repositories().droneModels();
        this.authz = AuthzRegistry.authorizationService();
    }

    public DroneModelDTO registerDroneModel(String name, String manufacturerName) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.DRONE_TECH);

        DroneModel model = new DroneModel(Designation.valueOf(name), Designation.valueOf(manufacturerName));
        return repo.save(model).toDTO();
    }

}
