package lapr4.integrations.dslplugin.application;

import lapr4.integrations.dslplugin.dto.DSLDescription;

import java.io.IOException;
import java.io.InputStream;

public interface DSLImporter {

	DSLDescription importFrom(InputStream filename) throws IOException;

}
