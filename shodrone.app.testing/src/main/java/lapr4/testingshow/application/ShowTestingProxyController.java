package lapr4.testingshow.application;


import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.testingshow.csvprotocol.client.CsvSimulatorProtocolProxy;
import lapr4.testingshow.csvprotocol.client.FailedRequestException;

import java.io.IOException;
import java.util.List;

/**
 * Controller for test a show in the Simulator.
 *
 */
public class ShowTestingProxyController {

	private final CsvSimulatorProtocolProxy proxy = new CsvSimulatorProtocolProxy();


	public List<String> testShow(ShowProposalDTO showToTest)
			throws IllegalStateException, IOException, FailedRequestException {
		return proxy.testShow(showToTest);
	}

}
