package lapr4.droneinventory.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.droneinventory.dto.DroneDTO;

import java.io.Serial;
import java.util.Date;

@Entity
public class Drone implements AggregateRoot<DroneSerialNumber>, DTOable<DroneDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true)
    private DroneSerialNumber serialNumber;

    private Date acquisitionDate;

    @Enumerated(EnumType.STRING)
    private DroneState state;

    @ManyToOne(optional = false)
    private DroneModel model;

    public Drone(DroneSerialNumber serialNumber, Date acquisitionDate, DroneState state, DroneModel model) {
        Preconditions.noneNull(serialNumber, acquisitionDate, state, model);

        this.serialNumber = serialNumber;
        this.acquisitionDate = acquisitionDate;
        this.state = state;
        this.model = model;
    }

    protected Drone() {
        // for ORM only
    }

    @Override
    public boolean sameAs(Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public DroneSerialNumber identity() {
        return serialNumber;
    }

    public DroneState state() {
        return state;
    }

    public DroneModel model() {
        return model;
    }

    @Override
    public DroneDTO toDTO() {
        return new DroneDTO(
                serialNumber.toString(),
                acquisitionDate,
                state.toString(),
                model.identity().toString()
        );
    }

}
