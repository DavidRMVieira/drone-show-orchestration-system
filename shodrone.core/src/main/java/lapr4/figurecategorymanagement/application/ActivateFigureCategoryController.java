package lapr4.figurecategorymanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class ActivateFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryRepository repo;
    private final ListFigureCategoryService categoryService;

    public ActivateFigureCategoryController() {
        this.authz = AuthzRegistry.authorizationService();
        this.repo = PersistenceContext.repositories().figureCategories();
        this.categoryService = new ListFigureCategoryService();
    }

    public Iterable<FigureCategoryDTO> inactiveFigureCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);
        return categoryService.allInactiveFigureCategories();
    }

    public FigureCategoryDTO activateFigureCategory(final FigureCategoryDTO categoryDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        FigureCategory category = new FigureCategoryDTOParser(repo).valueOf(categoryDTO);
        category.activate();
        return repo.save(category).toDTO();
    }
}
