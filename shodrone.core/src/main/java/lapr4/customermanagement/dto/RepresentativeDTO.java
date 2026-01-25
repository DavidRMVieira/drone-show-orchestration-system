package lapr4.customermanagement.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

@DTO
@Data
public class RepresentativeDTO {

    private String email;
    private String position;
    private String customerVat;
    private String phoneNumber;
    private String name;
    private String shodroneEmail;

    public RepresentativeDTO(String email, String position, String customerVat, String shodroneEmail, String phoneNumber, String name) {
        this.email = email;
        this.position = position;
        this.customerVat = customerVat;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.shodroneEmail = shodroneEmail;
    }
}