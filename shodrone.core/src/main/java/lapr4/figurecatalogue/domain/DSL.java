package lapr4.figurecatalogue.domain;

import eapli.framework.domain.model.DomainEntities;
import eapli.framework.domain.model.DomainEntity;
import eapli.framework.general.domain.model.Description;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;

import java.io.Serial;

@Entity
public class DSL implements DomainEntity<DSLVersion> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(nullable = false)
    private Description description;

    @Column(unique = true, nullable = false)
    private DSLVersion dslVersion;

    public DSL(Description description, DSLVersion dslVersion) {
        Preconditions.noneNull(description, dslVersion);

        this.description = description;
        this.dslVersion = dslVersion;
    }

    protected DSL() {
        // for ORM only
    }

    @Override
    public boolean sameAs(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        DSL otherDSL = (DSL) other;

        return dslVersion != null && dslVersion.equals(otherDSL.dslVersion);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public DSLVersion identity() {
        return dslVersion;
    }

    public Description description() {
        return description;
    }

    public DSLVersion dslVersion() {
        return dslVersion;
    }

    @Override
    public String toString() {
        return "DSL{" +
                "description=" + description +
                ", dslVersion=" + dslVersion +
                '}';
    }
}