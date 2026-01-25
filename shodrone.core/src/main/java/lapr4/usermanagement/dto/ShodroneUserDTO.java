package lapr4.usermanagement.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

@DTO
@Data
public class ShodroneUserDTO {

    private String email;
    private String phoneNumber;
    private String name;
    private boolean active;

    public ShodroneUserDTO(String email, String phoneNumber, String name, boolean active) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.active = active;
    }
}
