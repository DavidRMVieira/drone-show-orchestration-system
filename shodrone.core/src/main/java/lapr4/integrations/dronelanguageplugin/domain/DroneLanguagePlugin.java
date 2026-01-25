package lapr4.integrations.dronelanguageplugin.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.integrations.sharedkernel.domain.FQClassName;

import java.io.Serial;

@Entity
public class DroneLanguagePlugin implements AggregateRoot<DroneLanguageVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true)
    private DroneLanguageVersion droneLanguageVersion;

    private FQClassName className;

    public DroneLanguagePlugin(DroneLanguageVersion droneLanguageVersion, FQClassName className) {
        Preconditions.noneNull(className, droneLanguageVersion);

        this.className = className;
        this.droneLanguageVersion = droneLanguageVersion;
    }

    protected DroneLanguagePlugin() {
        // for ORM only
    }

    @Override
    public boolean sameAs(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        DroneLanguagePlugin otherDSL = (DroneLanguagePlugin) other;

        return id != null && id.equals(otherDSL.id);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public DroneLanguageVersion identity() {
        return droneLanguageVersion;
    }

    public FQClassName className() {
        return className;
    }

    @Override
    public String toString() {
        return "DSLPlugin {" +
                ", className=" + className +
                ", dslVersion=" + droneLanguageVersion +
                '}';
    }
}