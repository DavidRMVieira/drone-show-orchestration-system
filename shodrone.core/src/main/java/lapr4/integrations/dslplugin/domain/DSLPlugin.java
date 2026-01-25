package lapr4.integrations.dslplugin.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.figurecatalogue.domain.DSLVersion;
import lapr4.integrations.sharedkernel.domain.FQClassName;

import java.io.Serial;

@Entity
public class DSLPlugin implements AggregateRoot<DSLVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(unique = true)
    private DSLVersion dslVersion;

    private FQClassName className;

    public DSLPlugin(DSLVersion dslVersion, FQClassName className) {
        Preconditions.noneNull(className, dslVersion);

        this.className = className;
        this.dslVersion = dslVersion;
    }

    protected DSLPlugin() {
        // for ORM only
    }

    @Override
    public boolean sameAs(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        DSLPlugin otherDSL = (DSLPlugin) other;

        return id != null && id.equals(otherDSL.id);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public DSLVersion identity() {
        return dslVersion;
    }

    public FQClassName className() {
        return className;
    }

    @Override
    public String toString() {
        return "DSLPlugin {" +
                ", className=" + className +
                ", dslVersion=" + dslVersion +
                '}';
    }
}