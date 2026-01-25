package lapr4.integrations.proposaltemplate.repositories;

import eapli.framework.domain.repositories.DomainRepository;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;

public interface ProposalTemplateRepository extends DomainRepository<ProposalTemplateVersion, ProposalTemplate> {

}
