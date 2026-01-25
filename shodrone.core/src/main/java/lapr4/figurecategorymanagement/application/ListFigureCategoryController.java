package lapr4.figurecategorymanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class ListFigureCategoryController {

    private final AuthorizationService authz;
    private final ListFigureCategoryService categoryService;

    public ListFigureCategoryController() {
        this.authz = AuthzRegistry.authorizationService();
        this.categoryService = new ListFigureCategoryService();
    }

    public Iterable<FigureCategoryDTO> listFigureCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_COLLABORATOR);
        return categoryService.allFigureCategories();
    }

}
