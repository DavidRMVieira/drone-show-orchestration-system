package lapr4.integrations.proposaltemplate.dto;

import eapli.framework.representations.dto.DTO;
import lombok.Data;

import java.util.Map;

@DTO
@Data
public class ProposalTemplateDTO {

    private String language;
    private String representativeName;
    private String companyName;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private String vatNumber;
    private String showProposalNumber;
    private String date;
    private String videoLink;
    private String insuranceAmount;
    private String crmManagerName;
    private String gpsLocation;
    private String dateEvent;
    private String timeEvent;
    private int durationEvent;
    private Map<String, Integer> drones;
    private Map<String, String> figures;

}