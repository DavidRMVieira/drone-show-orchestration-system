package lapr4;

import lapr4.integrations.dronelanguageplugin.domain.DroneLanguagePlugin;
import lapr4.integrations.dronelanguageplugin.domain.DroneLanguageVersion;
import lapr4.integrations.dronelanguageplugin.repositories.DroneLanguagePluginRepository;

class JpaDroneLanguagePluginRepository extends ShodroneJpaRepositoryBase<DroneLanguagePlugin, Long, DroneLanguageVersion>
        implements DroneLanguagePluginRepository {

    public JpaDroneLanguagePluginRepository() {
        super("droneLanguageVersion");
    }

}
