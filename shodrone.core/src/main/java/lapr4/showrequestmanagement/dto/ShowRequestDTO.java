package lapr4.showrequestmanagement.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

import java.util.Date;

@DTO
@Data
public class ShowRequestDTO {

    private Long id;
    private String place;
    private String duration;
    private Date date;
    private String state;
    private String customer;

    public ShowRequestDTO(Long id, String place, String duration, Date date, String state, String customer) {
        this.id = id;
        this.place = place;
        this.duration = duration;
        this.date = date;
        this.state = state;
        this.customer = customer;
    }

}
