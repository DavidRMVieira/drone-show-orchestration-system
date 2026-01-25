package lapr4.figurecategorymanagement.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.Designation;
import lapr4.figurecategorymanagement.domain.FigureCategory;

public interface FigureCategoryRepository extends DomainRepository<Designation, FigureCategory> {

    Iterable<FigureCategory> findAllActive();

    Iterable<FigureCategory> findAllInactive();

}
