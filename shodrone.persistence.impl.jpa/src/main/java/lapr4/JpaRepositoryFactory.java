package lapr4;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.authz.repositories.impl.jpa.JpaAutoTxUserRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventConsumptionRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventRecordRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.jpa.JpaAutoTxEventConsumptionRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.jpa.JpaAutoTxEventRecordRepository;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import lapr4.infrastructure.persistence.RepositoryFactory;

public class JpaRepositoryFactory implements RepositoryFactory {

	@Override
	public UserRepository users(final TransactionalContext autoTx) {
		return new JpaAutoTxUserRepository(autoTx);
	}

	@Override
	public UserRepository users() {
		return new JpaAutoTxUserRepository(Application.settings().persistenceUnitName(),
				Application.settings().extendedPersistenceProperties());
	}

	@Override
	public JpaShodroneUserRepository shodroneUsers(final TransactionalContext autoTx) {
		return new JpaShodroneUserRepository(autoTx);
	}

	@Override
	public JpaShodroneUserRepository shodroneUsers() {
		return new JpaShodroneUserRepository(Application.settings().persistenceUnitName());
	}

	@Override
	public JpaCustomerRepository customers(final TransactionalContext autoTx) {
		return new JpaCustomerRepository(autoTx);
	}

	@Override
	public JpaCustomerRepository customers() {
		return new JpaCustomerRepository(Application.settings().persistenceUnitName());
	}

	@Override
	public JpaRepresentativeRepository representatives(final TransactionalContext autoTx) {
		return new JpaRepresentativeRepository(autoTx);
	}

	@Override
	public JpaRepresentativeRepository representatives() {
		return new JpaRepresentativeRepository(Application.settings().persistenceUnitName());
	}

	@Override
	public JpaFigureCategoryRepository figureCategories() {
		return new JpaFigureCategoryRepository();
	}

	@Override
	public JpaCatalogue catalogue() {
		return new JpaCatalogue();
	}

	@Override
	public JpaShowRequestRepository showRequests() {
		return new JpaShowRequestRepository();
	}

	@Override
	public JpaDSLPluginRepository dslPlugins() {
		return new JpaDSLPluginRepository();
	}

	@Override
	public JpaDroneLanguagePluginRepository droneLanguagePlugins() {
		return new JpaDroneLanguagePluginRepository();
	}

	@Override
	public JpaProposalTemplateRepository proposalTemplates() {
		return new JpaProposalTemplateRepository();
	}

	@Override
	public JpaDroneRepository drones() {
		return new JpaDroneRepository();
	}

	@Override
	public JpaDroneModelRepository droneModels() {
		return new JpaDroneModelRepository();
	}

	@Override
	public JpaShowProposalRepository showProposals() {
		return new JpaShowProposalRepository();
	}

	@Override
	public TransactionalContext newTransactionalContext() {
		return JpaAutoTxRepository.buildTransactionalContext(Application.settings().persistenceUnitName(),
				Application.settings().extendedPersistenceProperties());
	}

	@Override
	public EventConsumptionRepository eventConsumption() {
		return new JpaAutoTxEventConsumptionRepository(Application.settings().persistenceUnitName(),
				Application.settings().extendedPersistenceProperties());
	}

	@Override
	public EventRecordRepository eventRecord() {
		return new JpaAutoTxEventRecordRepository(Application.settings().persistenceUnitName(),
				Application.settings().extendedPersistenceProperties());
	}
}
