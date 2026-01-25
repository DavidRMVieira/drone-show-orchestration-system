package lapr4.showproposal.application;

import eapli.framework.application.UseCaseController;
import lapr4.showproposal.csvprotocol.client.CsvCASProtocolProxy;
import lapr4.showproposal.csvprotocol.client.FailedRequestException;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;

import java.io.IOException;

@UseCaseController
public class ListScheduledShowsProxyController {

    private final CsvCASProtocolProxy proxy = new CsvCASProtocolProxy();


    public Iterable<ShowProposalDTO> listCustomerScheduledShows(String username, String password)
            throws IllegalStateException, IOException, FailedRequestException {
        return proxy.listCustomerScheduledShows(username, password);
    }

}
