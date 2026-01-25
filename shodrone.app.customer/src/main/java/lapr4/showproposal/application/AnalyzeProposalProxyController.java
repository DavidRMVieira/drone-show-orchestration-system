package lapr4.showproposal.application;

import lapr4.showproposal.csvprotocol.client.CsvCASProtocolProxy;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.io.IOException;

/**
 * Controller for analyzing show proposals.
 *
 */
public class AnalyzeProposalProxyController {

	private final CsvCASProtocolProxy proxy = new CsvCASProtocolProxy();

	/**
	 * Lists all show proposals of the representative customer.
	 *
	 * @param username credential username of the representative customer
	 * @param password credential password of the representative customer
	 * @return Iterable of ShowProposalDTO
	 *
	 * @throws IOException if there is an i/O error
	 * @throws IllegalStateException  if the controller is not in a valid state
	 * @throws FailedRequestException if the request fails
	 *
	 */
	public Iterable<ShowProposalDTO> listRepresentativeShowProposalsAwaitingResponse(String username, String password)
			throws IllegalStateException, IOException, FailedRequestException {
		return proxy.listRepresentativeProposalsAwaitingResponse(username, password);
	}

}
