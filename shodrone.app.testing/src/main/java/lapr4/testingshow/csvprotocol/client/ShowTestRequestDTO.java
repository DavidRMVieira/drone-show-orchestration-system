package lapr4.testingshow.csvprotocol.client;

import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lombok.Value;


/**
 * DTO for the request to test a show.
 */
@Value
public class ShowTestRequestDTO {

	private ShowProposalDTO showToTest;

	public String toRequest() {
		int duration = showToTest.duration();
		int numberOfDrones = showToTest.numberOfDrones();
		double latitude = showToTest.latitudeLocation();
		double longitude = showToTest.longitudeLocation();


		return "TEST_SHOW, " + duration + ", " + numberOfDrones + ", " + latitude + ", " + longitude;
	}
}
