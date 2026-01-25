package lapr4;

import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.integrations.dslplugin.domain.DSLPlugin;
import lapr4.integrations.dslplugin.repositories.DSLPluginRepository;

class JpaDSLPluginRepository extends ShodroneJpaRepositoryBase<DSLPlugin, Long, DSLVersion>
        implements DSLPluginRepository {

    public JpaDSLPluginRepository() {
        super("dslVersion");
    }

}
