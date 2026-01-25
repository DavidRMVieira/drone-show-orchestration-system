package lapr4.showproposal.application;

import eapli.framework.application.UseCaseController;
import lapr4.showproposal.csvprotocol.client.CsvCASProtocolProxy;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.io.IOException;

@UseCaseController
public class GetShowInfoProxyController {

    private final CsvCASProtocolProxy proxy = new CsvCASProtocolProxy();


    public Iterable<ShowProposalDTO> listCustomerShowProposals(String username, String password)
            throws IllegalStateException, IOException, FailedRequestException {
        return proxy.listCustomerShowProposals(username, password);
    }

}
