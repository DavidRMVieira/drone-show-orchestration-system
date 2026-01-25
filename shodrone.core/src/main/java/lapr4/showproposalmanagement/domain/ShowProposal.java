package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.money.domain.model.Money;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.customermanagement.domain.Representative;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.events.*;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.ShowRequest;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class ShowProposal implements AggregateRoot<Long>, DTOable<ShowProposalDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    private Location location;

    private Date date;

    private Duration duration;

    private VideoFile videoSimulation;

    private int numberOfDrones;

    private Money insuranceAmount;

    @Enumerated(EnumType.STRING)
    private ShowProposalState state;

    @ManyToOne(optional = false)
    private ShowRequest showRequest;

    @ManyToOne(optional = true)
    private Representative representative;

    @ElementCollection
    private final Set<DroneInShow> drones = new HashSet<>();

    @ElementCollection
    private final Set<FigureInShow> figures = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private final Set<AbstractShowProposalEvent> events = new HashSet<>();

    @Lob
    @Column(length = 5000)
    private String document;

    private String feedback;

    public ShowProposal(Location location, Date date, Duration duration, int numberOfDrones, Money insuranceAmount, ShowProposalState state, ShowRequest showRequest, Representative representative) {
        Preconditions.noneNull(location, date, duration, numberOfDrones, insuranceAmount, state, showRequest);
        Preconditions.nonNegative(numberOfDrones);

        this.location = location;
        this.date = date;
        this.duration = duration;
        this.numberOfDrones = numberOfDrones;
        this.insuranceAmount = insuranceAmount;
        this.state = state;
        this.showRequest = showRequest;
        this.representative = representative;
    }

    protected ShowProposal(){
        // For ORM
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        return DomainEntities.areEqual(this, other);
    }

    public void addDrone(final DroneInShow drone) {
        Preconditions.nonNull(drone);
        this.drones.removeIf(d -> d.droneModelName().equals(drone.droneModelName()));
        this.state = ShowProposalState.IN_CONSTRUCTION;
        this.drones.add(drone);
    }

    public void addFigure(final FigureInShow figure) {
        Preconditions.nonNull(figure);
        this.figures.removeIf(d -> d.figureCode().equals(figure.figureCode()));
        this.state = ShowProposalState.IN_CONSTRUCTION;
        this.figures.add(figure);
    }

    public void configureDrones(final Set<DroneInShow> newDrones) {
        Preconditions.nonNull(newDrones);
        this.drones.clear();
        this.state = ShowProposalState.IN_CONSTRUCTION;
        this.drones.addAll(newDrones);
    }

    public void configureFigures(final Set<FigureInShow> newFigures) {
        Preconditions.nonNull(newFigures);
        this.figures.clear();
        this.state = ShowProposalState.IN_CONSTRUCTION;
        this.figures.addAll(newFigures);
    }

    public void addVideoSimulation(final VideoFile video) {
        Preconditions.nonNull(video);
        this.state = ShowProposalState.IN_CONSTRUCTION;
        this.videoSimulation = video;
    }

    @Override
    public Long identity() {
        return this.id;
    }

    public ShowRequest showRequest() {
        return this.showRequest;
    }

    public ShowProposalState state() {
        return this.state;
    }

    public Representative representative() {
        return this.representative;
    }

    public void markAsReadyToSend() {
        this.state = ShowProposalState.READY_TO_SEND;
    }

    public void sent(ShowProposalSentEvent sentEvent) {
        this.state = ShowProposalState.AWAITING_RESPONSE;
        this.document = sentEvent.document();
        this.events.add(sentEvent);
    }

    public void acceptedByCustomer(ShowProposalAcceptedEvent acceptedEvent) {
        this.state = ShowProposalState.CUSTOMER_ACCEPTED;
        this.events.add(acceptedEvent);
    }

    public void rejectedByCustomer(ShowProposalRejectedEvent rejectedEvent) {
        this.state = ShowProposalState.REJECTED;
        this.events.add(rejectedEvent);
        this.feedback = rejectedEvent.feedback();
    }

    public void acceptedByCollaborator(ShowProposalAcceptedByCRMEvent acceptedEvent) {
        this.state = ShowProposalState.ACCEPTED;
        this.events.add(acceptedEvent);
    }

    public boolean isAwaitingResponse() {
        return this.state == ShowProposalState.AWAITING_RESPONSE;
    }

    public boolean isReadyToSend() {
        return this.state == ShowProposalState.READY_TO_SEND;
    }

    public boolean isAcceptedByCustomer() {
        return this.state == ShowProposalState.CUSTOMER_ACCEPTED;
    }

    public boolean hasRepresentative() {
        return this.representative != null;
    }

    public Date date() {
        return date;
    }

    public VideoFile videoSimulation() {
        return videoSimulation;
    }

    public Money insuranceAmount() {
        return insuranceAmount;
    }

    public Location location() {
        return location;
    }

    public int numberOfDrones() {
        return numberOfDrones;
    }

    public Set<DroneInShow> drones() {
        return drones;
    }

    public Set<FigureInShow> figures() {
        return figures;
    }

    @Override
    public ShowProposalDTO toDTO() {
        return new ShowProposalDTO(id, date, duration.inMinutes(), videoSimulation != null ? videoSimulation.toString() : null,
                numberOfDrones, insuranceAmount.amountAsDouble(), location.latitude(), location.longitude(),
                state.toString(), showRequest.identity(), representative != null ? representative.identity().toString() : null,
                drones.isEmpty() ? Set.of() : drones.stream().map(DroneInShow::toDTO).collect(Collectors.toSet()),
                figures.isEmpty() ? Set.of() : figures.stream().map(FigureInShow::toDTO).collect(Collectors.toSet()),
                document != null ? document : null, feedback != null ? feedback : null
        );
    }

    @Override
    public String toString() {
        return "ShowProposal{" +
                ", location=" + location +
                ", date=" + date +
                ", duration=" + duration +
                ", videoSimulation=" + videoSimulation +
                ", numberOfDrones=" + numberOfDrones +
                ", insuranceAmount=" + insuranceAmount +
                ", state=" + state +
                ", showRequest=" + showRequest +
                ", representative=" + representative +
                ", drones=" + drones +
                ", figures=" + figures +
                ", document='" + document +
                ", feedback='" + feedback +
                '}';
    }
}
