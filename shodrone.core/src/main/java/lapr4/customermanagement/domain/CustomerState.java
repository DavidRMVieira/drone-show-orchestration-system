package lapr4.customermanagement.domain;

public enum CustomerState {

    DELETED,
    INFRINGEMENT,
    CREATED;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}