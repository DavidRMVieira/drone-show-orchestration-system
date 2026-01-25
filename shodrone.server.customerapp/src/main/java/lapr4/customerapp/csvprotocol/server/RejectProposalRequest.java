package lapr4.customerapp.csvprotocol.server;

import eapli.framework.infrastructure.authz.application.Authenticator;
import lapr4.showproposalmanagement.application.EvaluateProposalController;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneRoles;

/**
 * Request to reject a proposal.
 */
public class RejectProposalRequest extends CustomerAppProtocolRequest {

    private final String username;
    private final String password;
    private final Long proposalId;
    private final String feedback;

    private final Authenticator authenticationService;

    public RejectProposalRequest(final EvaluateProposalController controller, Authenticator authenticationService,
                                 final String request, final String username, String password, Long proposalId, String feedback) {
        super(controller, request);
        this.authenticationService = authenticationService;

        this.username = username;
        this.password = password;
        this.proposalId = proposalId;
        this.feedback = feedback;
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
            final boolean result = evaluateController.rejectProposal(proposalId, feedback);
            return buildResponse(result);

        } catch (final Exception e) {
            return buildServerError(e.getMessage());
        }
    }

    private String buildResponse(final boolean result) {
        String response;
        if (result) {
            response = "Proposal rejected successfully.";
        } else {
            response = "Proposal is not in a state to be rejected.";
        }

        return "REJECT, \"" + response + "\"\n";
    }

}
