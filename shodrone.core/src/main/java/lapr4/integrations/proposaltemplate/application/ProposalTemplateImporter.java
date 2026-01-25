package lapr4.integrations.proposaltemplate.application;

import java.io.IOException;
import java.io.InputStream;

public interface ProposalTemplateImporter {

	String importFrom(InputStream filename) throws IOException;

}
