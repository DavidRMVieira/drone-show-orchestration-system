package lapr4.droneinventory.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

import java.util.Date;

@DTO
@Data
public class DroneDTO {

    private String serialNumber;
    private Date acquisitionDate;
    private String state;
    private String model;

    public DroneDTO(String serialNumber, Date acquisitionDate, String state, String model) {
        this.serialNumber = serialNumber;
        this.acquisitionDate = acquisitionDate;
        this.state = state;
        this.model = model;
    }

}
