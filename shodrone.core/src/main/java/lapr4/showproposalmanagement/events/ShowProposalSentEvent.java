package lapr4.showproposalmanagement.events;

import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import jakarta.persistence.*;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;

import java.io.Serial;

@Entity
public class ShowProposalSentEvent extends AbstractShowProposalEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private SystemUser crmCollaborator;

    @ManyToOne(optional = true)
    private Customer customer;

    @ManyToOne(optional = true)
    private Representative representative;

    @Lob
    @Column(length = 5000)
    private String document;

    public ShowProposalSentEvent(Long proposalID, SystemUser crmCollaborator, Customer customer, String document) {
        super(proposalID);
        this.crmCollaborator = crmCollaborator;
        this.customer = customer;
        this.representative = null;
        this.document = document;
    }

    public ShowProposalSentEvent(Long proposalID, SystemUser crmCollaborator, Representative representative, String document) {
        super(proposalID);
        this.crmCollaborator = crmCollaborator;
        this.customer = null;
        this.representative = representative;
        this.document = document;
    }

    protected ShowProposalSentEvent() {
        // for ORM
        super();
    }

    public String document() {
        return document;
    }

}

