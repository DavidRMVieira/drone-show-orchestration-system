package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.DomainFactory;
import eapli.framework.money.domain.model.Money;
import lapr4.customermanagement.domain.Representative;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.ShowRequest;

import java.util.Date;

public class ShowProposalBuilder implements DomainFactory<ShowProposal> {

    private Location location;
    private Date date;
    private Duration duration;
    private int numberOfDrones;
    private Money insuranceAmount;
    private ShowProposalState state;
    private ShowRequest showRequest;
    private Representative representative;

    public ShowProposalBuilder withLocation(double latitudeLocation, double longitudeLocation) {
        this.location = Location.valueOf(latitudeLocation, longitudeLocation);
        return this;
    }

    public ShowProposalBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public ShowProposalBuilder withDuration(int duration) {
        this.duration = Duration.valueOf(duration);
        return this;
    }

    public ShowProposalBuilder withNumberOfDrones(int numberOfDrones) {
        this.numberOfDrones = numberOfDrones;
        return this;
    }

    public ShowProposalBuilder withInsuranceAmount(double amount) {
        this.insuranceAmount = Money.euros(amount);
        return this;
    }

    public ShowProposalBuilder withState(ShowProposalState state) {
        this.state = state;
        return this;
    }

    public ShowProposalBuilder withShowRequest(ShowRequest showRequest) {
        this.showRequest = showRequest;
        return this;
    }

    public ShowProposalBuilder withRepresentative(Representative representative) {
        this.representative = representative;
        return this;
    }

    @Override
    public ShowProposal build() {
        return new ShowProposal(location, date, duration, numberOfDrones, insuranceAmount, state, showRequest, representative);
    }

}