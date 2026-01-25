package lapr4.showproposalmanagement.events;

import eapli.framework.infrastructure.authz.domain.model.Name;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lapr4.customermanagement.domain.Customer;

import java.io.Serial;

@Entity
public class ShowProposalAcceptedByCRMEvent extends AbstractShowProposalEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private SystemUser collaborator;

    public ShowProposalAcceptedByCRMEvent(Long proposalId, Customer customer, SystemUser collaborator) {
        super(proposalId);
        this.customer = customer;
        this.collaborator = collaborator;
    }

    protected ShowProposalAcceptedByCRMEvent() {
        // for ORM
        super();
    }

}