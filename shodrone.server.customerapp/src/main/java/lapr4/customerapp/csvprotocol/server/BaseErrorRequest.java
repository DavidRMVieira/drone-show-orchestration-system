package lapr4.customerapp.csvprotocol.server;

import eapli.framework.csv.CsvRecord;

/**
 * Base class for error requests in the customer app protocol.
 *
 */
public abstract class BaseErrorRequest extends CustomerAppProtocolRequest {

    private final String errorDescription;

    protected BaseErrorRequest(final String request, final String errorDescription) {
        super(request);
        this.errorDescription = errorDescription;
    }

    protected BaseErrorRequest(final String request) {
        super(request);
        this.errorDescription = null;
    }

    @Override
    public String execute() {
        // nothing to do, just build the response
        return buildResponse();
    }

    protected String buildResponse() {
        final Object[] fields = {
                messageType(),
                request,
                errorDescription
        };
        final boolean[] mask = { false, true, true };
        return CsvRecord.valueOf(fields, mask).toString() + "\n";
    }

    protected abstract String messageType();
}
