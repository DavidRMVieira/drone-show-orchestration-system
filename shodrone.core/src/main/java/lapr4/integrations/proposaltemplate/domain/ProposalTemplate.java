package lapr4.integrations.proposaltemplate.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.integrations.sharedkernel.domain.FQClassName;

import java.io.Serial;

@Entity
public class ProposalTemplate implements AggregateRoot<ProposalTemplateVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true)
    private ProposalTemplateVersion proposalTemplateVersion;

    private FQClassName className;

    public ProposalTemplate(ProposalTemplateVersion proposalTemplateVersion, FQClassName className) {
        Preconditions.noneNull(className, proposalTemplateVersion);

        this.className = className;
        this.proposalTemplateVersion = proposalTemplateVersion;
    }

    protected ProposalTemplate() {
        // for ORM only
    }

    @Override
    public boolean sameAs(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ProposalTemplate otherDSL = (ProposalTemplate) other;

        return id != null && id.equals(otherDSL.id);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public ProposalTemplateVersion identity() {
        return proposalTemplateVersion;
    }

    public FQClassName className() {
        return className;
    }

    @Override
    public String toString() {
        return "ProposalTemplatePlugin{" +
                "proposalTemplateVersion=" + proposalTemplateVersion +
                ", className=" + className +
                '}';
    }
}