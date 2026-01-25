package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.domain.FigureCode;
import lapr4.figurecatalogue.repositories.Catalogue;

import java.text.Normalizer;

public class InMemoryCatalogue extends InMemoryDomainRepository<Figure, FigureCode>
        implements Catalogue {

    static {
        InMemoryInitializer.init();
    }

    @Override
    public Iterable<Figure> findAllPublic() {
        return match(f -> !f.isExclusive());
    }

    @Override
    public Iterable<Figure> findAllActive() {
        return match(Figure::isActive);
    }

    @Override
    public Iterable<Figure> searchByCategoryAndKeyword(String categoryName, String keyword) {
        String normalizedCategory = normalize(categoryName);
        String normalizedKeyword = normalize(keyword);

        return match(f ->
                normalize(f.category().identity().toString()).contains(normalizedCategory) &&
                        f.keywords().stream()
                                .anyMatch(k -> normalize(k).contains(normalizedKeyword))
        );
    }

    @Override
    public Iterable<Figure> searchByCategory(String categoryName) {
        String normalizedCategory = normalize(categoryName);

        return match(f ->
                normalize(f.category().identity().toString()).contains(normalizedCategory)
        );
    }

    @Override
    public Iterable<Figure> searchByKeyword(String keyword) {
        String normalizedKeyword = normalize(keyword);

        return match(f ->
                f.keywords().stream()
                        .anyMatch(k -> normalize(k).contains(normalizedKeyword))
        );
    }

    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

}
