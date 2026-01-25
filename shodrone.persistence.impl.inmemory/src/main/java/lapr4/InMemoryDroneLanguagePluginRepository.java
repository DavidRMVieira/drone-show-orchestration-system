package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;

public class InMemoryDroneLanguagePluginRepository extends InMemoryDomainRepository<DroneLanguagePlugin, DroneLanguageVersion>
        implements DroneLanguagePluginRepository {

    static {
        InMemoryInitializer.init();
    }

}
