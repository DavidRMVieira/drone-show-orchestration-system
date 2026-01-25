package lapr4.testingshow.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.usermanagement.domain.ShodroneRoles;

/**
 * Controller for test a show in the Simulator.
 *
 */
public class ShowTestingController {

	private final AuthorizationService authz = AuthzRegistry.authorizationService();
	private final ListShowProposalService svc = new ListShowProposalService();

	/**
	 * Lists all shows prepared to be tested in the simulator.
	 *
	 * @return Iterable of ShowProposalDTO
	 */
	public Iterable<ShowProposalDTO> listShows() {
		authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.DRONE_TECH);

		return svc.allShowsCRMCollaboratorAccepted();
	}

}
