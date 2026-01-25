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
public class DecommissionFigureCatalogueController {

    private final Catalogue repo;
    private final AuthorizationService authz;

    public DecommissionFigureCatalogueController() {
        this.repo = PersistenceContext.repositories().catalogue();
        this.authz = AuthzRegistry.authorizationService();
    }

    public void decommissionFigure(FigureDTO figureDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER);
        Figure figure = new FigureDTOParser(repo).valueOf(figureDTO);
        figure.decommission();
        repo.save(figure);
    }

    public Iterable<FigureDTO> allFigures() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER);
        final Iterable<Figure> figures = repo.findAll();
        return FigureDTOParser.transformToDTO(figures);
    }

}
