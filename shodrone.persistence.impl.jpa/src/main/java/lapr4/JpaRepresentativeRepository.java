package lapr4;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.Username;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.repositories.RepresentativeRepository;

import java.util.Optional;

class JpaRepresentativeRepository extends JpaAutoTxRepository<Representative, Long, EmailAddress>
        implements RepresentativeRepository {

    public JpaRepresentativeRepository(final TransactionalContext autoTx) {
        super(autoTx, "email");
    }

    public JpaRepresentativeRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "email");
    }

    @Override
    public Iterable<Representative> findAllByCustomer(Customer customer) {
        final var query = entityManager().createQuery(
                "SELECT r FROM Representative r WHERE r.customer = :customer", Representative.class);
        query.setParameter("customer", customer);
        return query.getResultList();
    }

    @Override
    public Optional<Representative> findByUser(SystemUser user) {
        final var query = entityManager().createQuery(
                "SELECT r FROM Representative r WHERE r.user.systemUser = :user", Representative.class);
        query.setParameter("user", user);

        return query.getResultStream().findFirst();
    }
}
