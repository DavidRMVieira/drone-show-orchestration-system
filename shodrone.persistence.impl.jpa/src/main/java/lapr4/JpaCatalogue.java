package lapr4;

import jakarta.persistence.TypedQuery;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.domain.FigureCode;
import lapr4.figurecatalogue.repositories.Catalogue;

import java.text.Normalizer;

public class JpaCatalogue extends ShodroneJpaRepositoryBase<Figure, Long, FigureCode>
        implements Catalogue {

    public JpaCatalogue() {
        super("code");
    }

    @Override
    public Iterable<Figure> findAllPublic() {
        final TypedQuery<Figure> query = entityManager().createQuery(
                "SELECT f FROM Figure f WHERE f.client IS NULL", Figure.class);
        return query.getResultList();
    }

    @Override
    public Iterable<Figure> findAllActive() {
        final TypedQuery<Figure> query = entityManager().createQuery(
                "SELECT f FROM Figure f WHERE f.active = true", Figure.class);
        return query.getResultList();
    }


    @Override
    public Iterable<Figure> searchByCategoryAndKeyword(String categoryName, String keyword) {
        String normalizedCategory = normalize(categoryName);
        String normalizedKeyword = normalize(keyword);

        final TypedQuery<Figure> query = entityManager().createQuery(
                "SELECT f FROM Figure f " +
                        "JOIN f.figureCategory fc " +
                        "WHERE LOWER(fc.name.name) = :category " +
                        "AND EXISTS (" +
                        "   SELECT k FROM f.keywords k WHERE LOWER(k) = :keyword" +
                        ")",
                Figure.class);
        query.setParameter("category", normalizedCategory);
        query.setParameter("keyword", normalizedKeyword);
        return query.getResultList();
    }

    @Override
    public Iterable<Figure> searchByCategory(String categoryName) {
        String normalizedCategory = normalize(categoryName);

        final TypedQuery<Figure> query = entityManager().createQuery(
                "SELECT f FROM Figure f " +
                        "JOIN f.figureCategory fc " +
                        "WHERE LOWER(fc.name.name) = :category",
                Figure.class);
        query.setParameter("category", normalizedCategory);
        return query.getResultList();
    }

    @Override
    public Iterable<Figure> searchByKeyword(String keyword) {
        String normalizedKeyword = normalize(keyword);

        final TypedQuery<Figure> query = entityManager().createQuery(
                "SELECT f FROM Figure f " +
                        "WHERE EXISTS (" +
                        "   SELECT k FROM f.keywords k WHERE LOWER(k) = :keyword" +
                        ")",
                Figure.class);
        query.setParameter("keyword", normalizedKeyword);
        return query.getResultList();
    }

    private String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}
