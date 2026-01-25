package lapr4;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.authz.repositories.impl.inmemory.InMemoryUserRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventConsumptionRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventRecordRepository;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryTransactionalContext;
import lapr4.bootstrapers.ShodroneBootstrapper;
import lapr4.customermanagement.repositories.CustomerRepository;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.droneinventory.repositories.DroneModelRepository;
import lapr4.droneinventory.repositories.DroneRepository;
import lapr4.figurecatalogue.repositories.Catalogue;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.infrastructure.persistence.RepositoryFactory;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

public class InMemoryRepositoryFactory implements RepositoryFactory {

	@Override
	public TransactionalContext newTransactionalContext() {
		return new InMemoryTransactionalContext();
	}

	@Override
	public UserRepository users(final TransactionalContext tx) {
		final var repo = new InMemoryUserRepository();
		ShodroneBootstrapper.registerPowerUser(repo);
		return repo;
	}

	@Override
	public UserRepository users() {
		return users(null);
	}

	@Override
	public ShodroneUserRepository shodroneUsers() {
		return shodroneUsers(null);
	}

	@Override
	public ShodroneUserRepository shodroneUsers(final TransactionalContext tx) {
		return new InMemoryShodroneUserRepository();
	}

	@Override
	public CustomerRepository customers() {
		return customers(null);
	}

	@Override
	public CustomerRepository customers(final TransactionalContext tx) {
		return new InMemoryCustomerRepository();
	}

	@Override
	public RepresentativeRepository representatives() {
		return representatives(null);
	}

	@Override
	public RepresentativeRepository representatives(final TransactionalContext tx) {
		return new InMemoryRepresentativeRepository();
	}

	@Override
	public Catalogue catalogue() {
		return new InMemoryCatalogue();
	}

	@Override
	public FigureCategoryRepository figureCategories() {
		return new InMemoryFigureCategoryRepository();
	}

	@Override
	public ShowRequestRepository showRequests() {
		return new InMemoryShowRequestRepository();
	}

	@Override
	public DSLPluginRepository dslPlugins() {
		return new InMemoryDSLPluginRepository();
	}

	@Override
	public DroneLanguagePluginRepository droneLanguagePlugins() {
		return new InMemoryDroneLanguagePluginRepository();
	}

	@Override
	public ProposalTemplateRepository proposalTemplates() {
		return new InMemoryProposalTemplateRepository();
	}

	@Override
	public DroneRepository drones() {
		return new InMemoryDroneRepository();
	}

	@Override
	public DroneModelRepository droneModels() {
		return new InMemoryDroneModelRepository();
	}

	@Override
	public ShowProposalRepository showProposals() {
		return new InMemoryShowProposalRepository();
	}

	@Override
	public EventConsumptionRepository eventConsumption() {
		throw new IllegalStateException("Not implemented yet.");
	}

	@Override
	public EventRecordRepository eventRecord() {
		throw new IllegalStateException("Not implemented yet.");
	}

}
