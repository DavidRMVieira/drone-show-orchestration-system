package lapr4.droneinventory.domain;

public enum DroneState {

    ACTIVE,
    REMOVED,
    BROKEN;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}