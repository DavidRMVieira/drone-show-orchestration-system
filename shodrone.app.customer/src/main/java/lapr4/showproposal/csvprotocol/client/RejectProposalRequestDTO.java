package lapr4.showproposal.csvprotocol.client;

import lombok.Value;


/**
 * DTO for the request to reject a proposal.
 */
@Value
public class RejectProposalRequestDTO {

	private final String who;
	private String password;
	private long proposalId;
	private String feedback;

	public String toRequest() {
		return "REJECT_PROPOSAL, " + "\"" + who + "\"" + ", \"" + password + "\"" + ", " + proposalId + ", \"" + feedback + "\"";
	}
}
