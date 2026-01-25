package lapr4;

import eapli.framework.general.domain.model.Designation;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;

class JpaFigureCategoryRepository extends ShodroneJpaRepositoryBase<FigureCategory, Long, Designation>
        implements FigureCategoryRepository {

    public JpaFigureCategoryRepository() {
        super("name");
    }

    @Override
    public Iterable<FigureCategory> findAllActive() {
        return match("e.active = true");
    }

    @Override
    public Iterable<FigureCategory> findAllInactive() {
        return match("e.active = false");
    }

}