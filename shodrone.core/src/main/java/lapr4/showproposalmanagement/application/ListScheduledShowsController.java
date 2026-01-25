package lapr4.showproposalmanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class ListScheduledShowsController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final RepresentativeRepository representativeRepository = PersistenceContext.repositories().representatives();
    private final ListShowProposalService svc = new ListShowProposalService();

    public Iterable<ShowProposalDTO> listScheduledShowsByCustomer() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.REPRESENTATIVE);

        SystemUser currentUser = authz.session().get().authenticatedUser();
        Representative representative = representativeRepository.findByUser(currentUser).get();

        return svc.allScheduledShowsByCustomer(representative.customer());
    }

}
