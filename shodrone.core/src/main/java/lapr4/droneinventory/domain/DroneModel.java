package lapr4.droneinventory.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.droneinventory.dto.DroneModelDTO;

import java.io.Serial;

@Entity
public class DroneModel implements AggregateRoot<Designation>, DTOable<DroneModelDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @AttributeOverride(name = "name", column = @Column(name = "model_name", unique = true))
    private Designation modelName;

    @AttributeOverride(name = "name", column = @Column(name = "manufacturer_name"))
    private Designation manufacturerName;

    public DroneModel(Designation modelName, Designation manufacturerName) {
        Preconditions.noneNull(modelName, manufacturerName);

        this.modelName = modelName;
        this.manufacturerName = manufacturerName;
    }

    protected DroneModel() {
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
    public Designation identity() {
        return modelName;
    }

    @Override
    public DroneModelDTO toDTO() {
        return new DroneModelDTO(
                modelName.toString(),
                manufacturerName.toString()
        );
    }

}
