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
public class InactivateFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryRepository repo;
    private final ListFigureCategoryService categoryService;

    public InactivateFigureCategoryController() {
        this.authz = AuthzRegistry.authorizationService();
        this.repo = PersistenceContext.repositories().figureCategories();
        this.categoryService = new ListFigureCategoryService();
    }

    public Iterable<FigureCategoryDTO> activeFigureCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);
        return categoryService.allActiveFigureCategories();
    }

    public FigureCategoryDTO inactivateFigureCategory(final FigureCategoryDTO categoryDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        FigureCategory category = new FigureCategoryDTOParser(repo).valueOf(categoryDTO);
        category.inactivate();
        return repo.save(category).toDTO();
    }

}
