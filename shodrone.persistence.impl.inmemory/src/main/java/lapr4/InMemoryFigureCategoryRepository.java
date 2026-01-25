package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;

import eapli.framework.general.domain.model.Designation;

public class InMemoryFigureCategoryRepository extends InMemoryDomainRepository<FigureCategory, Designation>
        implements FigureCategoryRepository {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public Iterable<FigureCategory> findAllActive() {
        return match(FigureCategory::isActive);
    }

    @Override
    public Iterable<FigureCategory> findAllInactive() {
        return match(fc -> !fc.isActive());
    }
}
