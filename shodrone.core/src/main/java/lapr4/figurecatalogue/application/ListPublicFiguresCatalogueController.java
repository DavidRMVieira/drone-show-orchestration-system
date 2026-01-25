package lapr4.figurecatalogue.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecatalogue.repositories.Catalogue;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class ListPublicFiguresCatalogueController {

    private final Catalogue repo;
    private final AuthorizationService authz;

    public ListPublicFiguresCatalogueController() {
        this.repo = PersistenceContext.repositories().catalogue();
        this.authz = AuthzRegistry.authorizationService();
    }

    public Iterable<FigureDTO> listAllPublicFigures() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);
        final Iterable<Figure> publicFigures = repo.findAllPublic();
        return FigureDTOParser.transformToDTO(publicFigures);
    }

}
