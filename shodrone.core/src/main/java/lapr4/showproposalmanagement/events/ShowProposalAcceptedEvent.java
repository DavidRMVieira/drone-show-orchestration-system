package lapr4.showproposalmanagement.events;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;

import java.io.Serial;

@Entity
public class ShowProposalAcceptedEvent extends AbstractShowProposalEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Representative representative;

    public ShowProposalAcceptedEvent(Long proposalId, Customer customer, Representative representative) {
        super(proposalId);
        this.customer = customer;
        this.representative = representative;
    }

    protected ShowProposalAcceptedEvent() {
        // for ORM
        super();
    }

}

