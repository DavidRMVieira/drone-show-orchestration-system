package lapr4.customermanagement.domain;

public enum CustomerType {

    REGULAR,
    VIP;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
