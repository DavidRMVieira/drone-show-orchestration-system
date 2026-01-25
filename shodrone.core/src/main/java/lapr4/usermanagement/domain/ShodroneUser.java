package lapr4.usermanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.*;
import lapr4.usermanagement.dto.ShodroneUserDTO;

import java.io.Serial;

@Entity
public class ShodroneUser implements AggregateRoot<ShodroneEmail>, DTOable<ShodroneUserDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Version
    private Long version;

    @Column(unique = true)
    private ShodroneEmail email;

    @OneToOne(optional = false)
    private SystemUser systemUser;

    private PhoneNumber phoneNumber;

    public ShodroneUser(final SystemUser user, final ShodroneEmail email, final PhoneNumber phoneNumber) {
        Preconditions.noneNull(user, email, phoneNumber);

        this.systemUser = user;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    protected ShodroneUser() {
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

    public SystemUser user() {
        return this.systemUser;
    }

    @Override
    public ShodroneEmail identity() {
        return email;
    }

    public PhoneNumber phoneNumber() {
        return phoneNumber;
    }

    @Override
    public ShodroneUserDTO toDTO() {
        return new ShodroneUserDTO(
                email.toString(),
                phoneNumber.toString(),
                systemUser.name().toString(),
                systemUser.isActive()
        );
    }

    @Override
    public String toString() {
        return "ShodroneUser{" +
                ", email=" + email +
                ", systemUser=" + systemUser +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
