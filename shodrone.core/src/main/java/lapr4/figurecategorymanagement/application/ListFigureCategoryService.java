package lapr4.figurecategorymanagement.application;

import eapli.framework.application.ApplicationService;
import eapli.framework.general.domain.model.Designation;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.infrastructure.persistence.PersistenceContext;

@ApplicationService
public class ListFigureCategoryService {

    private final FigureCategoryRepository repo = PersistenceContext.repositories().figureCategories();

    public Iterable<FigureCategoryDTO> allFigureCategories() {
        final Iterable<FigureCategory> categories = repo.findAll();
        return FigureCategoryDTOParser.transformToDTO(categories);
    }

    public Iterable<FigureCategoryDTO> allActiveFigureCategories() {
        final Iterable<FigureCategory> activeCategories =  repo.findAllActive();
        return FigureCategoryDTOParser.transformToDTO(activeCategories);
    }

    public Iterable<FigureCategoryDTO> allInactiveFigureCategories() {
        final Iterable<FigureCategory> inactiveCategories = repo.findAllInactive();
        return FigureCategoryDTOParser.transformToDTO(inactiveCategories);
    }

    public FigureCategory findFigureCategoryByName(final String name) {
        return repo.ofIdentity(Designation.valueOf(name))
                .orElseThrow(() -> new IllegalArgumentException("Unknown figure category: " + name));
    }
}
