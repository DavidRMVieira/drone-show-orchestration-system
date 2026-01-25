package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lapr4.showproposalmanagement.dto.DroneInShowDTO;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class DroneInShow implements ValueObject, DTOable<DroneInShowDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String droneModelName;

    private int quantity;

    public DroneInShow(final String droneModelName, final int quantity) {
        Preconditions.noneNull(droneModelName, quantity);
        Preconditions.ensure(quantity > 0, "Quantity must be a positive value");

        this.droneModelName = droneModelName;
        this.quantity = quantity;
    }

    protected DroneInShow() {
        // for ORM
    }

    public static DroneInShow valueOf(final String droneModelName, final int quantity) {
        return new DroneInShow(droneModelName, quantity);
    }

    public String droneModelName() {
        return droneModelName;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public DroneInShowDTO toDTO() {
        return new DroneInShowDTO(droneModelName, quantity);
    }

    @Override
    public String toString() {
        return "DroneInShow{" +
                "droneModelName=" + droneModelName +
                ", quantity=" + quantity +
                '}';
    }
}
