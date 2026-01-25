package lapr4.customerapp.csvprotocol.server;

import eapli.framework.infrastructure.authz.application.Authenticator;
import lapr4.showproposalmanagement.application.AnalyzeProposalController;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneRoles;

/**
 * Request to list all proposals for a representative that are awaiting a response.
 */
public class ListRepresentativeProposalsAwaitingResponseRequest extends ListShowProposalsRequest {

    private final String username;
    private final String password;

    private final Authenticator authenticationService;

    public ListRepresentativeProposalsAwaitingResponseRequest(final AnalyzeProposalController controller, Authenticator authenticationService,
                                                              final String request, final String username, String password) {
        super(controller, request);
        this.authenticationService = authenticationService;

        this.username = username;
        this.password = password;
    }

    @Override
    public String execute() {

        ShodroneEmail representativeEmail;
        try {
            representativeEmail = ShodroneEmail.valueOf(username);
        } catch (final IllegalArgumentException e) {
            return buildBadRequest("Invalid user name!");
        }

        // authenticate
        authenticationService.authenticate(username, password, ShodroneRoles.REPRESENTATIVE);


        // execution
        try {
            final Iterable<ShowProposalDTO> proposalsDTO = analyzeController.getProposalsForCustomerAwaitingResponse();

            if (!proposalsDTO.iterator().hasNext()) {
                return buildBadRequest("No proposals found for the representative customer with email: " + representativeEmail);
            }

            return buildResponse(proposalsDTO);
        } catch (final Exception e) {
            return buildServerError(e.getMessage());
        }
    }
}
