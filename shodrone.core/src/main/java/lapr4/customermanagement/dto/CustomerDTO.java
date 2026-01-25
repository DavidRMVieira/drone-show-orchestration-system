package lapr4.customermanagement.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

@DTO
@Data
public class CustomerDTO {

    private String vatNumber;
    private String name;
    private String address;
    private String state;
    private String type;

    public CustomerDTO(String vatNumber, String name, String address, String state, String type) {
        this.vatNumber = vatNumber;
        this.name = name;
        this.address = address;
        this.state = state;
        this.type = type;
    }

}
