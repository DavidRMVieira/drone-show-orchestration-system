package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.droneinventory.apllication.ListDroneModelService;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.domain.DroneInShow;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UseCaseController
public class AddDroneProposalController {

    private final AuthorizationService authz;
    private final ListDroneModelService droneSvc;
    private final ListShowProposalService proposalSvc;
    private final ShowProposalRepository showProposalRepository;

    public AddDroneProposalController() {
        this.authz = AuthzRegistry.authorizationService();
        this.droneSvc = new ListDroneModelService();
        this.proposalSvc = new ListShowProposalService();
        this.showProposalRepository = PersistenceContext.repositories().showProposals();
    }

    public void addDroneProposal(final ShowProposalDTO selectedProposalDTO, final DroneModelDTO selectedDroneModelDTO, final int quantity) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedProposalDTO.id());
        DroneModel droneModel = droneSvc.findDroneModelByName(selectedDroneModelDTO.getName());

        long activeDronesNumber = droneSvc.countActiveDronesByModel(droneModel);
        if (quantity > activeDronesNumber) {
            throw new IllegalArgumentException("The requested quantity (" + quantity + ") exceeds the number of available active drones for this model (" + activeDronesNumber + ").");
        }

        showProposal.addDrone(new DroneInShow(droneModel.identity().toString(), quantity));

        showProposalRepository.save(showProposal);
    }

    public void configureDronesProposal(final ShowProposalDTO selectedProposalDTO, final List<DroneInShowDTO> dronesInShowDTOList) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedProposalDTO.id());

        Set<DroneInShow> dronesInShow = new HashSet<>();

        for (DroneInShowDTO dto : dronesInShowDTOList) {
            DroneModel droneModel = droneSvc.findDroneModelByName(dto.droneModelName());

            long activeDronesNumber = droneSvc.countActiveDronesByModel(droneModel);
            if (dto.quantity() > activeDronesNumber) {
                throw new IllegalArgumentException("Requested quantity (" + dto.quantity() + ") for drone model '"
                        + dto.droneModelName() + "' exceeds available active drones (" + activeDronesNumber + ").");
            }

            dronesInShow.add(new DroneInShow(droneModel.identity().toString(), dto.quantity()));
        }

        showProposal.configureDrones(dronesInShow);

        showProposalRepository.save(showProposal);
    }


    public Iterable<ShowProposalDTO> listShowProposals() {
        return proposalSvc.allShowProposals();
    }

    public Iterable<DroneModelDTO> listDroneModels() {
        return droneSvc.allDroneModels();
    }

}
