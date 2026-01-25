package lapr4.customermanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.infrastructure.authz.domain.model.Name;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.customermanagement.dto.CustomerDTO;

import java.io.Serial;

@Entity
public class Customer implements AggregateRoot<VAT>, DTOable<CustomerDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true)
    private VAT vatNumber;

    private Name name;

    private Address address;

    @Enumerated(EnumType.STRING)
    private CustomerState state;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    public Customer(final VAT vatNumber, final Name name, final Address address, final CustomerState state, final CustomerType type) {
        Preconditions.noneNull(vatNumber, name, address, state, type);

        this.vatNumber = vatNumber;
        this.name = name;
        this.address = address;
        this.state = state;
        this.type = type;
    }

    protected Customer() {
        // for ORM only
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
    public VAT identity() {
        return vatNumber;
    }

    public Name name() {
        return name;
    }

    public CustomerState state () {
        return state;
    }

    public Address address() {
        return address;
    }

    @Override
    public CustomerDTO toDTO() {
        return new CustomerDTO(
                vatNumber.toString(),
                name.toString(),
                address.toString(),
                state.toString(),
                type.toString()
        );
    }

    @Override
    public String toString() {
        return "Customer{" +
                "vatNumber=" + vatNumber +
                ", name=" + name +
                ", address=" + address +
                ", state=" + state +
                ", type=" + type +
                '}';
    }

}
