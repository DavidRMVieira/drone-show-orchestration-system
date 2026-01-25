package lapr4.integrations.proposaltemplate.application;

import eapli.framework.application.ApplicationService;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.dronelanguageplugin.application.ValidateDroneLanguageController;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.usermanagement.domain.ShodroneRoles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@ApplicationService
public class GenerateProposalDocumentService {

    private static final Logger LOGGER = LogManager.getLogger(ValidateDroneLanguageController.class);
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ProposalTemplateRepository pluginRepository = PersistenceContext.repositories().proposalTemplates();

    public String proposalDocument(ShowProposal showProposal, String language, String proposalTemplateVersion) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);

        Optional<ProposalTemplate> plugin = pluginRepository.ofIdentity(ProposalTemplateVersion.valueOf(proposalTemplateVersion));
        if (plugin.isEmpty()) {
            throw new IllegalArgumentException("No plugin registered for Proposal Template Version: " + proposalTemplateVersion);
        }

        File file = generateProposalFile(showProposal, language);

        InputStream content = null;
        try {
            content = new FileInputStream(file);
            final var className = plugin.get().className().toString();
            final var importer = buildImporter(className);

            String document = importer.importFrom(content);

            return document;

        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (final IOException e) {
                    LOGGER.error("Error closing the file {}", file.getAbsolutePath());
                }
            }
        }
    }

    private ProposalTemplateImporter buildImporter(String className) {
        try {
            return (ProposalTemplateImporter) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            LOGGER.error("Unable to dynamically load the Plugin", ex);
            throw new IllegalStateException("Unable to dynamically load the Plugin: " + className, ex);
        }
    }

    private File generateProposalFile(ShowProposal sp, String language) throws IOException {
        File tempFile = File.createTempFile("proposal_", ".txt");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Language: " + language + "\n");

            if (sp.hasRepresentative()) {
                writer.write("Representative: " + sp.representative().user().user().name() + "\n");
            }

            writer.write("Company: " + sp.showRequest().customer().name() + "\n");
            writer.write("Address: " + sp.showRequest().customer().address().street() + "; " + sp.showRequest().customer().address().postalCode() + "; " + sp.showRequest().customer().address().city() + "; " + sp.showRequest().customer().address().country() + "\n");
            writer.write("VAT: " + sp.showRequest().customer().identity() + "\n");
            writer.write("Proposal Number: " + sp.identity() + "\n");
            writer.write("Date: " + dateFormat.format(sp.date()) + "\n");
            writer.write("Video: " + sp.videoSimulation() + "\n");
            writer.write("Insurance Amount: " + sp.insuranceAmount().amount() + " " + (sp.insuranceAmount().currency() != null ? sp.insuranceAmount().currency() : "") + "\n");
            writer.write("CRM Manager: " + authz.session().get().authenticatedUser().name() + "\n");

            writer.write("Event GPS: " + sp.location().latitude() + ";" + sp.location().longitude() + "\n");
            writer.write("Event Date: " + dateFormat.format(sp.date()) + "\n");
            writer.write("Event Time: " + timeFormat.format(sp.date()) + "\n");
            writer.write("Event Duration: " + sp.numberOfDrones() + "\n");

            writer.write("Def Drone\n");
            for (var drone : sp.drones()) {
                writer.write(drone.droneModelName() + " - " + drone.quantity() + "\n");
            }
            writer.write("End\n");

            writer.write("Def Figure\n");
            for (var figure : sp.figures()) {
                writer.write(figure.coordinateX() + ";" + figure.coordinateY() + ";" + figure.coordinateZ()+ " - " + figure.figureCode() + "\n");
            }
            writer.write("End\n");

        }

        return tempFile;
    }

}
