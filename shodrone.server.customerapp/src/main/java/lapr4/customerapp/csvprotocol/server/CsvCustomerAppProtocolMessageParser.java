package lapr4.customerapp.csvprotocol.server;

import java.text.ParseException;

import lapr4.showproposalmanagement.application.AnalyzeProposalController;
import lapr4.showproposalmanagement.application.EvaluateProposalController;
import lapr4.showproposalmanagement.application.GetShowInfoController;
import lapr4.showproposalmanagement.application.ListScheduledShowsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eapli.framework.csv.util.CsvLineMarshaler;
import eapli.framework.infrastructure.authz.application.Authenticator;

/**
 * The message parser for the Customer App Protocol, which processes CSV
 *
 */
public class CsvCustomerAppProtocolMessageParser {

    private static final Logger LOGGER = LogManager.getLogger(CsvCustomerAppProtocolMessageParser.class);

    private final AnalyzeProposalController analyzeProposalController;
    private final EvaluateProposalController evaluateProposalController;
    private final GetShowInfoController getShowInfoController;
    private final ListScheduledShowsController listScheduledShowsController;

    private final Authenticator authenticationService;

    public CsvCustomerAppProtocolMessageParser(Authenticator authenticationService) {
        this.analyzeProposalController = new AnalyzeProposalController();
        this.evaluateProposalController = new EvaluateProposalController();
        this.getShowInfoController = new GetShowInfoController();
        this.listScheduledShowsController = new ListScheduledShowsController();

        this.authenticationService = authenticationService;
    }

    /**
     * Parse and build the request.
     *
     */
    public CustomerAppProtocolRequest parse(final String inputLine) {
        // as a fallback make sure we return unknown
        CustomerAppProtocolRequest request = new UnknownRequest(inputLine);

        // parse to determine which type of request and if it is syntactically valid
        String[] tokens;
        try {
            tokens = CsvLineMarshaler.tokenize(inputLine).toArray(new String[0]);
            if ("GET_REPRESENTATIVE_SHOW_PROPOSALS_AWAITING_RESPONSE".equals(tokens[0])) {
                request = parseListRepresentativeProposalsAwaitingResponse(inputLine, tokens);
            } else if ("ACCEPT_PROPOSAL".equals(tokens[0])) {
                request = parseAcceptProposal(inputLine, tokens);
            } else if ("REJECT_PROPOSAL".equals(tokens[0])) {
                request = parseRejectProposal(inputLine, tokens);
            } else if ("GET_CUSTOMER_SCHEDULED_SHOW_PROPOSALS".equals(tokens[0])) {
                request = parseListCustomerScheduledShows(inputLine, tokens);
            } else if ("GET_CUSTOMER_SHOW_PROPOSALS".equals(tokens[0])) {
                request = parseListCustomerShowProposals(inputLine, tokens);
            }

        } catch (final ParseException e) {
            LOGGER.warn("Unable to parse request: {}", inputLine);
            request = new BadRequest(inputLine, "Unable to parse request");
        }

        return request;
    }


    private CustomerAppProtocolRequest parseListRepresentativeProposalsAwaitingResponse(final String inputLine, final String[] tokens) {
        CustomerAppProtocolRequest request;

        if (tokens.length != 3) {
            request = new BadRequest(inputLine, "Wrong number of parameters");
        } else if (!isStringParam(tokens[1])) {
            request = new BadRequest(inputLine, "user id must be inside quotes");
        } else if (!isStringParam(tokens[2])) {
            request = new BadRequest(inputLine, "password must be inside quotes");
        } else {
            request = new ListRepresentativeProposalsAwaitingResponseRequest(analyzeProposalController, authenticationService, inputLine,
                    CsvLineMarshaler.unquote(tokens[1]), CsvLineMarshaler.unquote(tokens[2])
            );
        }

        return request;
    }

    private CustomerAppProtocolRequest parseAcceptProposal(final String inputLine, final String[] tokens) {
        CustomerAppProtocolRequest request;

        if (tokens.length != 4) {
            request = new BadRequest(inputLine, "Wrong number of parameters");
        } else if (!isStringParam(tokens[1])) {
            request = new BadRequest(inputLine, "user id must be inside quotes");
        } else if (!isStringParam(tokens[2])) {
            request = new BadRequest(inputLine, "password must be inside quotes");
        } else if (isStringParam(tokens[3])) {
            request = new BadRequest(inputLine, "proposal id must not be inside quotes");
        } else {
            request = new AcceptProposalRequest(evaluateProposalController, authenticationService, inputLine,
                    CsvLineMarshaler.unquote(tokens[1]), CsvLineMarshaler.unquote(tokens[2]),
                    Long.parseLong(CsvLineMarshaler.unquote(tokens[3])));
        }
        return request;
    }

    private CustomerAppProtocolRequest parseRejectProposal(final String inputLine, final String[] tokens) {
        CustomerAppProtocolRequest request;

        if (tokens.length != 5) {
            request = new BadRequest(inputLine, "Wrong number of parameters");
        } else if (!isStringParam(tokens[1])) {
            request = new BadRequest(inputLine, "user id must be inside quotes");
        } else if (!isStringParam(tokens[2])) {
            request = new BadRequest(inputLine, "password must be inside quotes");
        } else if (isStringParam(tokens[3])) {
            request = new BadRequest(inputLine, "proposal id must not be inside quotes");
        } else if (!isStringParam(tokens[4])) {
            request = new BadRequest(inputLine, "feedback must be inside quotes");
        } else {
            request = new RejectProposalRequest(evaluateProposalController, authenticationService, inputLine,
                    CsvLineMarshaler.unquote(tokens[1]), CsvLineMarshaler.unquote(tokens[2]),
                    Long.parseLong(CsvLineMarshaler.unquote(tokens[3])), CsvLineMarshaler.unquote(tokens[4]));
        }
        return request;
    }

    private CustomerAppProtocolRequest parseListCustomerScheduledShows(final String inputLine, final String[] tokens) {
        CustomerAppProtocolRequest request;

        if (tokens.length != 3) {
            request = new BadRequest(inputLine, "Wrong number of parameters");
        } else if (!isStringParam(tokens[1])) {
            request = new BadRequest(inputLine, "user id must be inside quotes");
        } else if (!isStringParam(tokens[2])) {
            request = new BadRequest(inputLine, "password must be inside quotes");
        } else {
            request = new ListCustomerScheduledShowsRequest(listScheduledShowsController, authenticationService, inputLine,
                    CsvLineMarshaler.unquote(tokens[1]), CsvLineMarshaler.unquote(tokens[2])
            );
        }

        return request;
    }

    private CustomerAppProtocolRequest parseListCustomerShowProposals(final String inputLine, final String[] tokens) {
        CustomerAppProtocolRequest request;

        if (tokens.length != 3) {
            request = new BadRequest(inputLine, "Wrong number of parameters");
        } else if (!isStringParam(tokens[1])) {
            request = new BadRequest(inputLine, "user id must be inside quotes");
        } else if (!isStringParam(tokens[2])) {
            request = new BadRequest(inputLine, "password must be inside quotes");
        } else {
            request = new ListCustomerProposalsRequest(getShowInfoController, authenticationService, inputLine,
                    CsvLineMarshaler.unquote(tokens[1]), CsvLineMarshaler.unquote(tokens[2])
            );
        }

        return request;
    }


    private boolean isStringParam(final String string) {
        return string.length() >= 2 && string.charAt(0) == '"' && string.charAt(string.length() - 1) == '"';
    }
}
