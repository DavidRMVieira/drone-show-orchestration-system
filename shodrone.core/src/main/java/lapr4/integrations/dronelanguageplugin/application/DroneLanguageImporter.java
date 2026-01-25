package lapr4.integrations.dronelanguageplugin.application;

import lapr4.integrations.dronelanguageplugin.dto.DroneLanguage;

import java.io.IOException;
import java.io.InputStream;

public interface DroneLanguageImporter {

    DroneLanguage importFrom(InputStream filename) throws IOException;

}