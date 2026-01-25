package lapr4.showrequestmanagement.domain;

public enum ShowRequestState {

    PENDING,
    ACCEPTED,
    REJECTED;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
