package lapr4.showproposal.application;

import eapli.framework.application.UseCaseController;
import lapr4.showproposal.csvprotocol.client.CsvCASProtocolProxy;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;


import java.io.IOException;

@UseCaseController
public class EvaluateProposalProxyController {

    private final CsvCASProtocolProxy proxy = new CsvCASProtocolProxy();

    public String acceptProposal(String username, String password, ShowProposalDTO showProposalDTO)
			throws IllegalStateException, IOException, FailedRequestException {
        return proxy.acceptProposal(username, password, showProposalDTO.id());
    }

    public String rejectProposal(String username, String password, ShowProposalDTO showProposalDTO, String feedback)
            throws IllegalStateException, IOException, FailedRequestException {
        return proxy.rejectProposal(username, password, showProposalDTO.id(), feedback);
    }

}
