package lapr4.droneinventory.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class DroneSerialNumber implements ValueObject, Comparable<DroneSerialNumber> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String serialNumber;

    public DroneSerialNumber(String serialNumber) {
        Preconditions.nonEmpty(serialNumber, "Serial Number should neither be null nor empty");

        this.serialNumber = serialNumber;
    }

    protected DroneSerialNumber() {
        // for ORM only
    }

    public static DroneSerialNumber valueOf(final String serialNumber) {
        return new DroneSerialNumber(serialNumber);
    }

    @Override
    public String toString() {
        return serialNumber;
    }

    @Override
    public int compareTo(final DroneSerialNumber other) {
        return serialNumber.compareTo(other.serialNumber);
    }
}
