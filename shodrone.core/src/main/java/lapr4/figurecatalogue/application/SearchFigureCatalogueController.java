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
public class SearchFigureCatalogueController {

    private final Catalogue repo;
    private final AuthorizationService authz;

    public SearchFigureCatalogueController() {
        this.repo = PersistenceContext.repositories().catalogue();
        this.authz = AuthzRegistry.authorizationService();
    }

    public Iterable<FigureDTO> searchByCategoryAndKeyword(String categoryName, String keyword) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);
        final Iterable<Figure> figures = repo.searchByCategoryAndKeyword(categoryName, keyword);
        return FigureDTOParser.transformToDTO(figures);
    }

    public Iterable<FigureDTO> searchByCategory(String categoryName) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);
        final Iterable<Figure> figures = repo.searchByCategory(categoryName);
        return FigureDTOParser.transformToDTO(figures);
    }

    public Iterable<FigureDTO> searchByKeyword(String keyword) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);
        final Iterable<Figure> figures = repo.searchByKeyword(keyword);
        return FigureDTOParser.transformToDTO(figures);
    }

}
