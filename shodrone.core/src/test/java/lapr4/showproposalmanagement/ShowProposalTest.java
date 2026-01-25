package lapr4.showproposalmanagement;

import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.money.domain.model.Money;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.util.CustomerTestUtil;
import lapr4.customermanagement.util.RepresentativeTestUtil;
import lapr4.showproposalmanagement.domain.*;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedByCRMEvent;
import lapr4.showproposalmanagement.events.ShowProposalAcceptedEvent;
import lapr4.showproposalmanagement.events.ShowProposalRejectedEvent;
import lapr4.showproposalmanagement.events.ShowProposalSentEvent;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.Place;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.domain.ShowRequestState;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalTest {

    private static final Location VALID_LOCATION = new Location(41.1579, -8.6291);
    private static final Date VALID_DATE = new Date();
    private static final Duration VALID_DURATION = new Duration(90);
    private static final int VALID_DRONES = 5;
    private static final Money VALID_INSURANCE = Money.euros(200.0);
    private static final ShowProposalState VALID_STATE = ShowProposalState.ACCEPTED;
    private static final ShowRequest VALID_SHOW_REQUEST = new ShowRequest(new Place("Main Stage"), VALID_DATE, VALID_DURATION,
            ShowRequestState.PENDING, CustomerTestUtil.dummyCustomer());
    private static final Representative VALID_REPRESENTATIVE = RepresentativeTestUtil.dummyRepresentative(EmailAddress.valueOf("daniel@email.com"));

    @Test
    void ensureValidShowProposalIsCreated() {
        final var proposal = new ShowProposal(
                VALID_LOCATION,
                VALID_DATE,
                VALID_DURATION,
                VALID_DRONES,
                VALID_INSURANCE,
                VALID_STATE,
                VALID_SHOW_REQUEST,
                VALID_REPRESENTATIVE
        );
        assertNotNull(proposal);
        assertEquals(VALID_LOCATION.latitude(), proposal.toDTO().latitudeLocation());
        assertEquals(VALID_LOCATION.longitude(), proposal.toDTO().longitudeLocation());
        assertEquals(VALID_DATE, proposal.toDTO().date());
        assertEquals(VALID_DRONES, proposal.toDTO().numberOfDrones());
        assertEquals(VALID_INSURANCE.amountAsDouble(), proposal.toDTO().insuranceAmount());
        assertEquals(VALID_SHOW_REQUEST.identity(), proposal.toDTO().showRequestId());
        assertEquals(VALID_REPRESENTATIVE.identity().toString(), proposal.toDTO().representativeEmail());
    }

    @Test
    void ensureAllParametersAreRequired() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(null, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(VALID_LOCATION, null, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(VALID_LOCATION, VALID_DATE, null, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, null, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE,null, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, null, VALID_REPRESENTATIVE));
    }

    @Test
    void ensureNegativeDroneCountNotAllowed() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, -1, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE));
    }

    @Test
    void ensureShowProposalCanBeCreatedWithoutRepresentative() {
        final var proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST,
                null // no representative assigned
        );

        assertNotNull(proposal);
        assertNull(proposal.toDTO().representativeEmail());
    }

    @Test
    void ensureDroneIsAddedSuccessfully() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        DroneInShow drone1 = new DroneInShow("ModelX", 10);
        proposal.addDrone(drone1);

        assertTrue(proposal.toString().contains("ModelX"));
    }

    @Test
    void ensureDronesAreConfiguredCorrectly() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        DroneInShow drone1 = new DroneInShow("ModelA", 20);
        DroneInShow drone2 = new DroneInShow("ModelB", 25);

        Set<DroneInShow> droneSet = new HashSet<>();
        droneSet.add(drone1);
        droneSet.add(drone2);

        proposal.configureDrones(droneSet);

        assertTrue(proposal.toString().contains("ModelA"));
        assertTrue(proposal.toString().contains("ModelB"));
    }

    @Test
    void ensureConfigureDronesReplacesOldOnes() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        DroneInShow oldDrone = new DroneInShow("OldModel", 30);
        proposal.addDrone(oldDrone);

        DroneInShow newDrone = new DroneInShow("NewModel", 40);
        Set<DroneInShow> newDroneSet = new HashSet<>();
        newDroneSet.add(newDrone);

        proposal.configureDrones(newDroneSet);

        assertFalse(proposal.toString().contains("OldModel"));
        assertTrue(proposal.toString().contains("NewModel"));
    }

    @Test
    void ensureAddDroneThrowsIfNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(IllegalArgumentException.class, () -> proposal.addDrone(null));
    }

    @Test
    void ensureConfigureDronesThrowsIfNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(IllegalArgumentException.class, () -> proposal.configureDrones(null));
    }

    @Test
    void ensureFigureIsAddedSuccessfully() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE,
                VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        FigureInShow figure = new FigureInShow("FigureA", 10, 20, 30);
        proposal.addFigure(figure);

        assertTrue(proposal.toString().contains("FigureA"));
    }

    @Test
    void ensureFiguresAreConfiguredCorrectly() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE,
                VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        FigureInShow figure1 = new FigureInShow("FigureX", 1, 2, 3);
        FigureInShow figure2 = new FigureInShow("FigureY", 4, 5, 6);

        Set<FigureInShow> figures = new HashSet<>();
        figures.add(figure1);
        figures.add(figure2);

        proposal.configureFigures(figures);

        assertTrue(proposal.toString().contains("FigureX"));
        assertTrue(proposal.toString().contains("FigureY"));
    }

    @Test
    void ensureConfigureFiguresReplacesOldOnes() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE,
                VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        FigureInShow oldFigure = new FigureInShow("OldFigure", 0, 0, 0);
        proposal.addFigure(oldFigure);

        FigureInShow newFigure = new FigureInShow("NewFigure", 9, 9, 9);
        Set<FigureInShow> newFigures = new HashSet<>();
        newFigures.add(newFigure);

        proposal.configureFigures(newFigures);

        assertFalse(proposal.toString().contains("OldFigure"));
        assertTrue(proposal.toString().contains("NewFigure"));
    }

    @Test
    void ensureAddFigureThrowsIfNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE,
                VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(IllegalArgumentException.class, () -> proposal.addFigure(null));
    }

    @Test
    void ensureConfigureFigureThrowsIfNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE,
                VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(IllegalArgumentException.class, () -> proposal.configureFigures(null));
    }

    @Test
    void ensureVideoSimulationIsAddedSuccessfully() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        VideoFile video = new VideoFile("https://youtube.com");
        proposal.addVideoSimulation(video);

        assertTrue(proposal.toString().contains("https://youtube.com"));
    }

    @Test
    void ensureAddVideoSimulationThrowsIfNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES, VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(IllegalArgumentException.class, () -> proposal.addVideoSimulation(null));
    }

    @Test
    void ensureSentChangesStateToAwaitingResponse() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        ShowProposalSentEvent sentEvent = new ShowProposalSentEvent(1L, null, VALID_REPRESENTATIVE, "document.pdf");

        proposal.sent(sentEvent);

        assertEquals(ShowProposalState.AWAITING_RESPONSE, proposal.state());
    }

    @Test
    void ensureSentThrowsIfEventIsNull() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        assertThrows(NullPointerException.class, () -> proposal.sent(null));
    }

    @Test
    void ensureProposalIsAcceptedByCollaborator() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        ShowProposalAcceptedByCRMEvent sentEvent = new ShowProposalAcceptedByCRMEvent(1L, null, null);

        proposal.acceptedByCollaborator(sentEvent);

        assertEquals(ShowProposalState.ACCEPTED, proposal.state());
    }

    @Test
    void ensureProposalIsAcceptedByCustomer() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        ShowProposalAcceptedEvent sentEvent = new ShowProposalAcceptedEvent(1L, null, null);

        proposal.acceptedByCustomer(sentEvent);

        assertEquals(ShowProposalState.CUSTOMER_ACCEPTED, proposal.state());
    }

    @Test
    void ensureProposalIsRejectedByCustomer() {
        ShowProposal proposal = new ShowProposal(
                VALID_LOCATION, VALID_DATE, VALID_DURATION, VALID_DRONES,
                VALID_INSURANCE, VALID_STATE, VALID_SHOW_REQUEST, VALID_REPRESENTATIVE
        );

        ShowProposalRejectedEvent sentEvent = new ShowProposalRejectedEvent(1L, null, null, "feedback");

        proposal.rejectedByCustomer(sentEvent);

        assertEquals(ShowProposalState.REJECTED, proposal.state());
    }

}
