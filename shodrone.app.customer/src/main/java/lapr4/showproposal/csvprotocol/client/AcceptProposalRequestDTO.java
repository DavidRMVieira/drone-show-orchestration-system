package lapr4.showproposal.csvprotocol.client;

import lombok.Value;


/**
 * DTO for the request to accept a proposal.
 */
@Value
public class AcceptProposalRequestDTO {

	private final String who;
	private String password;
	private long proposalId;

	public String toRequest() {
		return "ACCEPT_PROPOSAL, " + "\"" + who + "\"" + ", \"" + password + "\"" + ", " + proposalId;
	}
}
