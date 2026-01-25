package lapr4;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplate;
import lapr4.integrations.proposaltemplate.domain.ProposalTemplateVersion;
import lapr4.integrations.proposaltemplate.repositories.ProposalTemplateRepository;

public class InMemoryProposalTemplateRepository extends InMemoryDomainRepository<ProposalTemplate, ProposalTemplateVersion>
        implements ProposalTemplateRepository {

    static {
        InMemoryInitializer.init();
    }

}
