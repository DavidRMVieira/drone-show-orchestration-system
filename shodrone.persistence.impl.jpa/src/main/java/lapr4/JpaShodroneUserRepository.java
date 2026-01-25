package lapr4;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

class JpaShodroneUserRepository extends JpaAutoTxRepository<ShodroneUser, Long, ShodroneEmail>
        implements ShodroneUserRepository {

    public JpaShodroneUserRepository(final TransactionalContext autoTx) {
        super(autoTx, "email");
    }

    public JpaShodroneUserRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "email");
    }

    @Override
    public Iterable<ShodroneUser> findAllActive() {
        return match("e.systemUser.active = true");
    }

    @Override
    public Iterable<ShodroneUser> findAllDisabled() {
        return match("e.systemUser.active = false");
    }

}
