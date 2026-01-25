package lapr4.showproposal.csvprotocol.client;

import lombok.Value;


/**
 * DTO for the request to get shows proposals for a customer.
 */
@Value
public class GetCustomerShowProposalsRequestDTO {

	private final String who;
	private String password;

	public String toRequest() {
		return "GET_CUSTOMER_SHOW_PROPOSALS, " + "\"" + who + "\"" + ", \"" + password + "\"";
	}
}
