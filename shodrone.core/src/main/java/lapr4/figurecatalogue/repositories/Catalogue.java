package lapr4.figurecatalogue.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.domain.FigureCode;

public interface Catalogue extends DomainRepository<FigureCode, Figure> {

    Iterable<Figure> findAllPublic();

    Iterable<Figure> findAllActive();

    Iterable<Figure> searchByCategoryAndKeyword(String categoryName, String keyword);

    Iterable<Figure> searchByCategory(String categoryName);

    Iterable<Figure> searchByKeyword(String keyword);

}
