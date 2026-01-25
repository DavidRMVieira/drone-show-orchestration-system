package lapr4.showrequestmanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.customermanagement.domain.Customer;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;

import java.io.Serial;
import java.util.Date;

@Entity
public class ShowRequest implements AggregateRoot<Long>, DTOable<ShowRequestDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    private Place place;

    private Date date;

    private Duration duration;

    private ShowRequestState state;

    @ManyToOne(optional = false)
    private Customer customer;

    public ShowRequest(Place place, Date date, Duration duration, ShowRequestState state, Customer customer) {
        Preconditions.noneNull(place, date, duration, state, customer);

        this.place = place;
        this.date = date;
        this.duration = duration;
        this.state = state;
        this.customer = customer;
    }

    protected ShowRequest(){
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

    public void changePlaceTo(final Place newPlace) {
        Preconditions.nonNull(newPlace);
        this.place = newPlace;
    }

    public void changeDateTo(final Date newDate) {
        Preconditions.nonNull(newDate);
        this.date = newDate;
    }

    public void changeDurationTo(final Duration newDuration) {
        Preconditions.nonNull(newDuration);
        this.duration = newDuration;
    }

    public void changeStateTo(final ShowRequestState newState) {
        Preconditions.nonNull(newState);
        this.state = newState;
    }

    public Customer customer () {
        return customer;
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public ShowRequestDTO toDTO() {
        return new ShowRequestDTO(
                id,
                place.toString(),
                duration.toString(),
                date,
                state.toString(),
                customer.identity().toString()
        );
    }

    @Override
    public String toString() {
        return "ShowRequest{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", state=" + state +
                ", customer=" + customer +
                '}';
    }
}
