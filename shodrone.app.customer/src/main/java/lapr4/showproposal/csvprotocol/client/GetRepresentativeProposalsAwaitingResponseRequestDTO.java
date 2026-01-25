package lapr4.showproposal.csvprotocol.client;

import lombok.Value;


/**
 * DTO for the request to get shows proposals for a representative awaiting response.
 */
@Value
public class GetRepresentativeProposalsAwaitingResponseRequestDTO {

	private final String who;
	private String password;

	public String toRequest() {
		return "GET_REPRESENTATIVE_SHOW_PROPOSALS_AWAITING_RESPONSE, " + "\"" + who + "\"" + ", \"" + password + "\"";
	}
}
