package lapr4.integrations.proposaltemplate.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showproposalmanagement.application.ListShowProposalService;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;
import lapr4.usermanagement.domain.ShodroneRoles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

@UseCaseController
public class ValidateProposalDocumentController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ShowProposalRepository repo = PersistenceContext.repositories().showProposals();
    private final GenerateProposalDocumentService documentSvc = new GenerateProposalDocumentService();
    private final ListShowProposalService proposalSvc = new ListShowProposalService();

    public String validateProposalDocument(ShowProposalDTO selectedShowProposalDTO, String language, String proposalTemplateVersion) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER);

        ShowProposal showProposal = proposalSvc.findShowProposalByID(selectedShowProposalDTO.id());

        String document = documentSvc.proposalDocument(showProposal, language, proposalTemplateVersion);

        if (!showProposal.isReadyToSend()) {
            showProposal.markAsReadyToSend();
            repo.save(showProposal);
        }

        return document;
    }

    public Iterable<ShowProposalDTO> listShowProposals() {
        return proposalSvc.allShowProposals();
    }

}