package lapr4.customerapp.csvprotocol.server;

import eapli.framework.infrastructure.authz.application.Authenticator;
import lapr4.showproposalmanagement.application.EvaluateProposalController;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneRoles;

/**
 * Request to accept a proposal.
 */
public class AcceptProposalRequest extends CustomerAppProtocolRequest {

    private final String username;
    private final String password;
    private final Long proposalId;

    private final Authenticator authenticationService;

    public AcceptProposalRequest(final EvaluateProposalController controller, Authenticator authenticationService,
                                 final String request, final String username, String password, Long proposalId) {
        super(controller, request);
        this.authenticationService = authenticationService;

        this.username = username;
        this.password = password;
        this.proposalId = proposalId;
    }

    @Override
    public String execute() {

        try {
            ShodroneEmail.valueOf(username);
        } catch (final IllegalArgumentException e) {
            return buildBadRequest("Invalid user name!");
        }

        // authenticate
        authenticationService.authenticate(username, password, ShodroneRoles.REPRESENTATIVE);


        // execution
        try {
            final boolean result = evaluateController.acceptProposal(proposalId);
            return buildResponse(result);

        } catch (final Exception e) {
            return buildServerError(e.getMessage());
        }
    }

    private String buildResponse(final boolean result) {
        String response;
        if (result) {
            response = "Proposal accepted successfully.";
        } else {
            response = "Proposal is not in a state to be accepted.";
        }

        return "ACCEPT, \"" + response + "\"\n";
    }

}
