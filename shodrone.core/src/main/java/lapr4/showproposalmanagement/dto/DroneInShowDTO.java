package lapr4.showproposalmanagement.dto;

import eapli.framework.representations.dto.DTO;

@DTO
public class DroneInShowDTO {

    private String droneModelName;
    private int quantity;

    public DroneInShowDTO(String droneModelName, int quantity) {
        this.droneModelName = droneModelName;
        this.quantity = quantity;
    }

    public String droneModelName() {
        return droneModelName;
    }
    public int quantity() {
        return quantity;
    }

}
