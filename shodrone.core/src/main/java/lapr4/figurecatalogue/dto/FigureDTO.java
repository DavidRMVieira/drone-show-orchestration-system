package lapr4.figurecatalogue.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

import java.util.Set;

@DTO
@Data
public class FigureDTO {

    private String code;
    private String figureVersion;
    private String description;
    private String dslDescription;
    private String dslVersion;
    private String type;
    private String figureCategory;
    private Set<String> keywords;
    private String customer;

    public FigureDTO(String code, String figureVersion, String description, String dslDescription,
                     String dslVersion, String type, String figureCategory,
                     Set<String> keywords, String customer) {
        this.code = code;
        this.figureVersion = figureVersion;
        this.description = description;
        this.dslDescription = dslDescription;
        this.dslVersion = dslVersion;
        this.type = type;
        this.figureCategory = figureCategory;
        this.keywords = keywords;
        this.customer = customer;
    }

}