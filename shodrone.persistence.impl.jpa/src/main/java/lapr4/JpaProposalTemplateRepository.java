package lapr4;

import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;

class JpaProposalTemplateRepository extends ShodroneJpaRepositoryBase<ProposalTemplate, Long, ProposalTemplateVersion>
        implements ProposalTemplateRepository {

    public JpaProposalTemplateRepository() {
        super("proposalTemplateVersion");
    }

}
