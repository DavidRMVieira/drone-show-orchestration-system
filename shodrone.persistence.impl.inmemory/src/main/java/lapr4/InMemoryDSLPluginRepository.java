package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.integrations.dslplugin.domain.DSLPlugin;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;

public class InMemoryDSLPluginRepository extends InMemoryDomainRepository<DSLPlugin, DSLVersion>
        implements DSLPluginRepository {

    static {
        InMemoryInitializer.init();
    }

}
