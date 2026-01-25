package lapr4.testingshow.csvprotocol.client;

import java.io.Serial;

/**
 * Exception thrown when a request to the server fails.
 */
public class FailedRequestException extends Exception {

	/**
	 * Serial version UID for serialization.
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public FailedRequestException(String string) {
		super(string);
	}

}
