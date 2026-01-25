package lapr4.figurecategorymanagement.application;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.domain.ShodroneRoles;

public class AddFigureCategoryController {

    private final AuthorizationService authz;
    private final FigureCategoryRepository repo;

    public AddFigureCategoryController () {
        this.authz = AuthzRegistry.authorizationService();
        this.repo = PersistenceContext.repositories().figureCategories();
    }

    public FigureCategoryDTO addFigureCategory(String name, String description) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER);

        FigureCategory figureCategory = new FigureCategory(Designation.valueOf(name.toLowerCase()), Description.valueOf(description));
        return repo.save(figureCategory).toDTO();
    }

}
