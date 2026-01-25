package lapr4;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.VAT;
import lapr4.customermanagement.repositories.CustomerRepository;

class JpaCustomerRepository extends JpaAutoTxRepository<Customer, Long, VAT>
        implements CustomerRepository {

    public JpaCustomerRepository(final TransactionalContext autoTx) {
        super(autoTx, "vatNumber");
    }

    public JpaCustomerRepository(final String puname) {
        super(puname, Application.settings().extendedPersistenceProperties(), "vatNumber");
    }

}
