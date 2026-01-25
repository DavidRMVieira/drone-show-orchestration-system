package lapr4.figurecatalogue.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.general.domain.model.Description;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.customermanagement.domain.Customer;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecategorymanagement.domain.FigureCategory;

import java.io.Serial;
import java.util.Calendar;
import java.util.Set;

@Entity
public class Figure implements AggregateRoot<FigureCode>, DTOable<FigureDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true, nullable = false)
    private FigureCode code;

    @Column(nullable = false)
    private FigureVersion figureVersion;

    @Column(nullable = false)
    private Description description;

    @OneToOne(cascade = CascadeType.ALL)
    private DSL dsl;

    @Enumerated(EnumType.STRING)
    private FigureType type;

    @ManyToOne
    private Customer client;

    @ManyToOne
    private FigureCategory figureCategory;

    @ElementCollection
    private Set<String> keywords;

    private boolean active;

    @Temporal(TemporalType.DATE)
    private Calendar deactivatedOn;

    public Figure(Description description, DSL dsl, FigureCode code, FigureType figureType, FigureVersion figureVersion, Customer customer, FigureCategory figureCategory, Set<String> keywords) {
        Preconditions.noneNull(description, dsl, code, figureType, figureVersion, figureCategory, keywords);

        this.description = description;
        this.dsl = dsl;
        this.code = code;
        this.type = figureType;
        this.figureVersion = figureVersion;
        this.client = customer;
        this.figureCategory = figureCategory;
        this.keywords = keywords;
        this.active = true;
    }

    protected Figure() {
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
    public FigureCode identity() {
        return code;
    }

    public FigureCategory category() {
        return figureCategory;
    }

    public Set<String> keywords() {
        return keywords;
    }

    public boolean isExclusive() {
        return client != null;
    }

    public boolean isActive() {
        return active;
    }

    public void decommission() {
        if (!this.active) {
            throw new IllegalStateException("Cannot decommission an inactive figure");
        } else {
            this.active = false;
            this.deactivatedOn = CurrentTimeCalendars.now();
        }
    }

    @Override
    public FigureDTO toDTO() {
        return new FigureDTO(
                code.toString(),
                figureVersion.toString(),
                description.toString(),
                dsl.description().toString(),
                dsl.dslVersion().toString(),
                type.toString(),
                figureCategory.identity().toString(),
                keywords,
                isExclusive() ? client.identity().toString() : null
        );
    }

    @Override
    public String toString() {
        return "Figure{" +
                "code=" + code +
                ", figureVersion=" + figureVersion +
                ", description=" + description +
                ", dsl=" + dsl +
                ", type=" + type +
                ", client=" + client +
                ", figureCategory=" + figureCategory +
                ", keywords=" + keywords +
                '}';
    }
}