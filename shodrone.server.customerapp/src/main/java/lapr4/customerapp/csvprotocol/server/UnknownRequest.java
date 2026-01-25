package lapr4.customerapp.csvprotocol.server;

/**
 * Represents an unknown request in the CSV protocol.
 *
 */
public class UnknownRequest extends BaseErrorRequest {

    public UnknownRequest(final String inputLine) {
        super(inputLine);
    }

    @Override
    protected String messageType() {
        return "UNKNOWN_REQUEST";
    }
}
