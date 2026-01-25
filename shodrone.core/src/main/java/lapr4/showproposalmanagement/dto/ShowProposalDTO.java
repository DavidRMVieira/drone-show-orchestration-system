package lapr4.showproposalmanagement.dto;

import eapli.framework.representations.dto.DTO;
import java.util.Date;
import java.util.Set;

@DTO
public class ShowProposalDTO {

    private Long id;
    private Date date;
    private int duration;
    private String videoSimulation;
    private int numberOfDrones;
    private double insuranceAmount;
    private double latitudeLocation;
    private double longitudeLocation;
    private String state;
    private Long showRequestId;
    private String representativeEmail;
    private Set<DroneInShowDTO> drones;
    private Set<FigureInShowDTO> figures;
    private String documentContent;
    private String feedback;
    private String documentLink;

    public ShowProposalDTO(Long id, Date date, int duration,
                           String videoSimulation, int numberOfDrones, double insuranceAmount,
                           double latitudeLocation, double longitudeLocation, String state,
                           Long showRequestId, String representativeEmail, String documentContent, String documentLink,
                           Set<DroneInShowDTO> drones, Set<FigureInShowDTO> figures, String feedback) {
        this.id = id;
        this.latitudeLocation = latitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.date = date;
        this.duration = duration;
        this.videoSimulation = videoSimulation;
        this.numberOfDrones = numberOfDrones;
        this.insuranceAmount = insuranceAmount;
        this.state = state;
        this.showRequestId = showRequestId;
        this.representativeEmail = representativeEmail;
        this.drones = drones;
        this.figures = figures;
        this.documentContent = documentContent;
        this.documentLink = documentLink;
        this.feedback = feedback;
    }

    public ShowProposalDTO(Long id, Date date, int duration,
                           String videoSimulation, int numberOfDrones, double insuranceAmount,
                           double latitudeLocation, double longitudeLocation, String state,
                           Long showRequestId, String representativeEmail,
                           Set<DroneInShowDTO> drones, Set<FigureInShowDTO> figures, String documentContent, String feedback) {
        this.id = id;
        this.latitudeLocation = latitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.date = date;
        this.duration = duration;
        this.videoSimulation = videoSimulation;
        this.numberOfDrones = numberOfDrones;
        this.insuranceAmount = insuranceAmount;
        this.state = state;
        this.showRequestId = showRequestId;
        this.representativeEmail = representativeEmail;
        this.drones = drones;
        this.figures = figures;
        this.documentContent = documentContent;
        this.feedback = feedback;
    }

    public ShowProposalDTO(Long id, Date date, int duration,
                           int numberOfDrones, double insuranceAmount, double latitudeLocation,
                           double longitudeLocation, String state,
                           String representativeEmail, String videoSimulation) {
        this.id = id;
        this.latitudeLocation = latitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.date = date;
        this.duration = duration;
        this.videoSimulation = videoSimulation;
        this.numberOfDrones = numberOfDrones;
        this.insuranceAmount = insuranceAmount;
        this.state = state;
        this.representativeEmail = representativeEmail;
    }

    public ShowProposalDTO(Date date, int duration, int numberOfDrones, double insuranceAmount,
                           double latitudeLocation, double longitudeLocation) {
        this.latitudeLocation = latitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.date = date;
        this.duration = duration;
        this.numberOfDrones = numberOfDrones;
        this.insuranceAmount = insuranceAmount;
    }

    public Long id() {
        return id;
    }

    public Date date() {
        return date;
    }

    public int duration() {
        return duration;
    }

    public String videoSimulation() {
        return videoSimulation;
    }

    public int numberOfDrones() {
        return numberOfDrones;
    }

    public double insuranceAmount() {
        return insuranceAmount;
    }

    public double latitudeLocation() {
        return latitudeLocation;
    }

    public double longitudeLocation() {
        return longitudeLocation;
    }

    public String state() {
        return state;
    }

    public Long showRequestId() {
        return showRequestId;
    }

    public String representativeEmail() {
        return representativeEmail;
    }

    public Set<DroneInShowDTO> drones() {
        return drones;
    }

    public Set<FigureInShowDTO> figures() {
        return figures;
    }

    public String document() {return documentContent;}

    public String documentLink() {return documentLink;}

    public String feedback() {return feedback;}

}
