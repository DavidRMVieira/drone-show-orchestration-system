package lapr4.figurecategorymanagement.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class ChangeFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryRepository repo;
    private final ListFigureCategoryService categoryService;

    public ChangeFigureCategoryController() {
        this.authz = AuthzRegistry.authorizationService();
        this.repo = PersistenceContext.repositories().figureCategories();
        this.categoryService = new ListFigureCategoryService();
    }

    public FigureCategoryDTO changeFigureCategoryName(final String newName, FigureCategoryDTO categoryDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        FigureCategory category = new FigureCategoryDTOParser(repo).valueOf(categoryDTO);
        category.changeNameTo(Designation.valueOf(newName));

        return repo.save(category).toDTO();
    }

    public FigureCategoryDTO changeFigureCategoryDescription(final String newDescription, FigureCategoryDTO categoryDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        FigureCategory category = new FigureCategoryDTOParser(repo).valueOf(categoryDTO);
        category.changeDescriptionTo(Description.valueOf(newDescription));

        return repo.save(category).toDTO();
    }

    public Iterable<FigureCategoryDTO> allFigureCategories() {
        return categoryService.allFigureCategories();
    }
}
