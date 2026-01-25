package lapr4.customerapp.csvprotocol.server;

import lapr4.showproposalmanagement.application.AnalyzeProposalController;
import lapr4.showproposalmanagement.application.EvaluateProposalController;
import lapr4.showproposalmanagement.application.GetShowInfoController;
import lapr4.showproposalmanagement.application.ListScheduledShowsController;

/**
 * Abstract class representing a request in the customer app protocol.
 *
 */
public abstract class CustomerAppProtocolRequest {

    protected final String request;
    protected  AnalyzeProposalController analyzeController = null;
    protected EvaluateProposalController evaluateController = null;
    protected GetShowInfoController getShowInfoController = null;
    protected ListScheduledShowsController listScheduledShowsController = null;

    protected CustomerAppProtocolRequest(final AnalyzeProposalController controller, final String inputRequest) {
        this.request = inputRequest;
        this.analyzeController = controller;
    }

    protected CustomerAppProtocolRequest(final EvaluateProposalController controller, final String inputRequest) {
        this.request = inputRequest;
        this.evaluateController = controller;
    }

    protected CustomerAppProtocolRequest(final GetShowInfoController controller, final String inputRequest) {
        this.request = inputRequest;
        this.getShowInfoController = controller;
    }

    protected CustomerAppProtocolRequest(final ListScheduledShowsController controller, final String inputRequest) {
        this.request = inputRequest;
        this.listScheduledShowsController = controller;
    }

    protected CustomerAppProtocolRequest(final String inputRequest) {
        this.request = inputRequest;
    }

    /**
     * Executes the requested action and builds the response to the client.
     *
     * @return the response to send back to the client
     */
    public abstract String execute();

    /**
     * Indicates the object is a goodbye message, that is, a message that will close the
     * connection to the client.
     *
     * @return {@code true} if the object is a goodbye message.
     */
    public boolean isGoodbye() {
        return false;
    }

    protected String buildServerError(final String errorDescription) {
        final BaseErrorRequest r = new BaseErrorRequest(request, errorDescription) {

            @Override
            protected String messageType() {
                return "SERVER_ERROR";
            }

        };
        return r.buildResponse();
    }

    protected String buildBadRequest(final String errorDescription) {
        final BaseErrorRequest r = new BaseErrorRequest(request, errorDescription) {

            @Override
            protected String messageType() {
                return "BAD_REQUEST";
            }

        };
        return r.buildResponse();
    }
}
