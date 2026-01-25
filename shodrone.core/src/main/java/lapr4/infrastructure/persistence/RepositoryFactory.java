package lapr4.infrastructure.persistence;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventConsumptionRepository;
import eapli.framework.infrastructure.pubsub.impl.simplepersistent.repositories.EventRecordRepository;
import lapr4.customermanagement.repositories.CustomerRepository;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.droneinventory.repositories.DroneModelRepository;
import lapr4.droneinventory.repositories.DroneRepository;
import lapr4.figurecatalogue.repositories.Catalogue;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

public interface RepositoryFactory {

    TransactionalContext newTransactionalContext();

    UserRepository users(TransactionalContext autoTx);

    UserRepository users();

    ShodroneUserRepository shodroneUsers(TransactionalContext autoTx);

    ShodroneUserRepository shodroneUsers();

    CustomerRepository customers(TransactionalContext autoTx);

    CustomerRepository customers();

    FigureCategoryRepository figureCategories();

    RepresentativeRepository representatives(TransactionalContext autoTx);

    RepresentativeRepository representatives();

    Catalogue catalogue();

    ShowRequestRepository showRequests();

    DSLPluginRepository dslPlugins();

    DroneLanguagePluginRepository droneLanguagePlugins();

    ProposalTemplateRepository proposalTemplates();

    DroneRepository drones();

    DroneModelRepository droneModels();

    ShowProposalRepository showProposals();

    EventConsumptionRepository eventConsumption();

    EventRecordRepository eventRecord();

}
