package lapr4.droneinventory.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

@DTO
@Data
public class DroneModelDTO {

    private String name;
    private String manufacturerName;

    public DroneModelDTO(String name, String manufacturerName) {
        this.name = name;
        this.manufacturerName = manufacturerName;
    }

    public DroneModelDTO(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
