package lapr4.customermanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.customermanagement.dto.RepresentativeDTO;
import lapr4.usermanagement.domain.ShodroneUser;

import java.io.Serial;

@Entity
public class Representative implements AggregateRoot<EmailAddress>, DTOable<RepresentativeDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true)
    private EmailAddress email;

    private Designation position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @OneToOne(optional = false)
    private ShodroneUser user;

    public Representative(final EmailAddress email, final Customer customer, final ShodroneUser user, final Designation position) {
        Preconditions.noneNull(email, customer, user, position);

        this.email = email;
        this.customer = customer;
        this.user = user;
        this.position = position;
    }

    protected Representative() {
        // for ORM
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
    public EmailAddress identity() {
        return email;
    }

    public Customer customer() {
        return customer;
    }

    public ShodroneUser user() {
        return user;
    }

    @Override
    public RepresentativeDTO toDTO() {
        return new RepresentativeDTO(
                email.toString(),
                position.toString(),
                customer.identity().toString(),
                user.identity().toString(),
                user.phoneNumber().toString(),
                user.user().name().toString()
        );
    }

    @Override
    public String toString() {
        return "Representative{" +
                ", email=" + email +
                ", position=" + position +
                ", customer=" + customer +
                ", user=" + user +
                '}';
    }

}
