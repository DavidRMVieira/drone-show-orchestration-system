package lapr4;

import eapli.framework.infrastructure.repositories.impl.jpa.JpaTransactionalRepository;

/* package */ class ShodroneJpaRepositoryBase<T, K, I>
        extends JpaTransactionalRepository<T, K, I> {

    ShodroneJpaRepositoryBase(final String persistenceUnitName, final String identityFieldName) {
        super(persistenceUnitName, Application.settings().extendedPersistenceProperties(),
                identityFieldName);
    }

    ShodroneJpaRepositoryBase(final String identityFieldName) {
        super(Application.settings().persistenceUnitName(),
                Application.settings().extendedPersistenceProperties(), identityFieldName);
    }

}
