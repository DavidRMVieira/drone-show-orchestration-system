package lapr4.integrations.proposaltemplate.application;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;
import lapr4.integrations.sharedkernel.domain.FQClassName;
import lapr4.usermanagement.domain.ShodroneRoles;

@UseCaseController
public class RegisterProposalTemplateController {

    private final AuthorizationService authorizationService = AuthzRegistry.authorizationService();
    private final ProposalTemplateRepository repository = PersistenceContext.repositories().proposalTemplates();;

    public ProposalTemplate registerProposalTemplate(final String proposalTemplateVersion, final String className) {
        authorizationService.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_MANAGER);

        final var plugin = new ProposalTemplate(ProposalTemplateVersion.valueOf(proposalTemplateVersion), FQClassName.valueOf(className));

        return repository.save(plugin);
    }
}
