package lapr4.testingshow.csvprotocol.client;

import java.util.Arrays;
import java.util.List;


/**
 * Marshaler/ Unmarshal for the Simulator Csv Protocol. It is responsible for
 * marshaling the data to create a proper network message and unmarshal the
 * network message to the proper DTO class
 */
class CsvServerResponseParser {


    public List<String> parseResponseMessageTestShow(final List<String> response)
            throws FailedRequestException {
        if (response == null) {
            return Arrays.asList("SERVER_ERROR: No response from server", "Please try again later");
        }

        checkForErrorMessage(response);

        return response;
    }


    private void checkForErrorMessage(final List<String> response) throws FailedRequestException {
        final String[] tokens = response.get(0).split(",");
        final String messageType = tokens[0];

        if (messageType.equals("SERVER_ERROR") || messageType.equals("BAD_REQUEST")
                || messageType.equals("UNKNOWN_REQUEST") || messageType.equals("ERROR_IN_REQUEST")) {
            throw new FailedRequestException(messageType + ":" + tokens[tokens.length - 1]);
        }
    }
}
