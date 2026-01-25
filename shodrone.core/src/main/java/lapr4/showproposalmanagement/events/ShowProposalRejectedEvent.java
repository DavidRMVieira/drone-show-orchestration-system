package lapr4.showproposalmanagement.events;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.Representative;

import java.io.Serial;

@Entity
public class ShowProposalRejectedEvent extends AbstractShowProposalEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Representative representative;

    private String feedback;

    public ShowProposalRejectedEvent(Long proposalId, Customer customer, Representative representative, String feedback) {
        super(proposalId);
        this.customer = customer;
        this.representative = representative;
        this.feedback = feedback;
    }

    protected ShowProposalRejectedEvent() {
        // for ORM
        super();
    }

    public String feedback() {
        return feedback;
    }

}

