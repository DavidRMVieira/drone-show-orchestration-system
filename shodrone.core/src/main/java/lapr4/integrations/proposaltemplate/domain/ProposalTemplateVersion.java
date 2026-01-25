package lapr4.integrations.proposaltemplate.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class ProposalTemplateVersion implements ValueObject, Comparable<ProposalTemplateVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String templateVersion;

    public ProposalTemplateVersion(String templateVersion) {
        Preconditions.nonEmpty(templateVersion, "Version should neither be null nor empty");

        this.templateVersion = templateVersion;
    }

    protected ProposalTemplateVersion() {
        // for ORM only
    }

    public static ProposalTemplateVersion valueOf(final String version) {
        return new ProposalTemplateVersion(version);
    }

    @Override
    public String toString() {
        return templateVersion;
    }

    @Override
    public int compareTo(final ProposalTemplateVersion other) {
        return templateVersion.compareTo(other.templateVersion);
    }
}