package lapr4.showproposalmanagement.events;

import eapli.framework.domain.events.DomainEvent;
import eapli.framework.domain.events.DomainEventBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serial;

@Entity
public abstract class AbstractShowProposalEvent extends DomainEventBase implements DomainEvent {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long pk;

	private Long proposalID;

	protected AbstractShowProposalEvent(final Long proposalID) {
		this.proposalID = proposalID;
	}

	protected AbstractShowProposalEvent() {
		// for ORM
	}

	public Long proposalID() {
		return proposalID;
	}
}
