package lapr4.showproposalmanagement.domain;

public enum ShowProposalState {

    IN_CONSTRUCTION,
    READY_TO_SEND,
    AWAITING_RESPONSE,
    CUSTOMER_ACCEPTED,
    ACCEPTED,
    REJECTED;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
}
