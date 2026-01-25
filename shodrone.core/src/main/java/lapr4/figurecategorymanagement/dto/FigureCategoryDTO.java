package lapr4.figurecategorymanagement.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

@DTO
@Data
public class FigureCategoryDTO {

    private String name;
    private String description;
    private boolean active;

    public FigureCategoryDTO(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

}
