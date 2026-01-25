package lapr4.figurecategorymanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;

import java.io.Serial;
import java.util.Calendar;

@Entity
public class FigureCategory implements AggregateRoot<Designation>, DTOable<FigureCategoryDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true)
    private Designation name;

    private Description description;

    private boolean active;

    @Temporal(TemporalType.DATE)
    private Calendar createdOn;

    @Temporal(TemporalType.DATE)
    private Calendar lastStateChangeOn;

    public FigureCategory(final Designation name, final Description description) {
        Preconditions.noneNull(name, description);

        this.name = name;
        this.description = description;
        this.active = true;
        this.createdOn = CurrentTimeCalendars.now();
    }

    protected FigureCategory() {
        // For ORM
    }

    public void inactivate() {
        if (!this.active) {
            throw new IllegalStateException("Cannot inactivate an inactive category");
        } else {
            this.active = false;
            this.lastStateChangeOn = CurrentTimeCalendars.now();
        }
    }

    public void activate() {
        if (this.active) {
            throw new IllegalStateException("Unable to activate an active category");
        } else {
            this.active = true;
            this.lastStateChangeOn = CurrentTimeCalendars.now();
        }
    }

    public boolean isActive () {
        return active;
    }

    public void changeNameTo(final Designation newName) {
        Preconditions.nonNull(newName);
        this.name = newName;
    }

    public void changeDescriptionTo(final Description newDescription) {
        Preconditions.nonNull(newDescription);
        this.description = newDescription;
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

    @Override
    public Designation identity() {
        return name;
    }

    @Override
    public FigureCategoryDTO toDTO() {
        return new FigureCategoryDTO(
                name.toString(),
                description.toString(),
                active
        );
    }

    @Override
    public String toString() {
        return "FigureCategory{" +
                ", name=" + name +
                ", description=" + description +
                ", active=" + active +
                '}';
    }
}
