package lapr4.customermanagement.application;

import eapli.framework.application.ApplicationService;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.Name;
import eapli.framework.infrastructure.authz.domain.model.Role;
import lapr4.customermanagement.domain.*;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.usermanagement.application.RegisterShodroneUserService;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.customermanagement.repositories.CustomerRepository;
import lapr4.usermanagement.domain.ShodroneUser;

import java.util.HashSet;
import java.util.Set;

@ApplicationService
public class RegisterCustomerService {

    private final TransactionalContext txCtx;
    private final AuthorizationService authz;
    private final RegisterShodroneUserService userSvc;
    private final CustomerRepository customerRepo;
    private final RepresentativeRepository representativeRepo;

    public RegisterCustomerService() {
        this.txCtx = PersistenceContext.repositories().newTransactionalContext();
        this.authz = AuthzRegistry.authorizationService();
        this.userSvc = new RegisterShodroneUserService(txCtx);
        this.customerRepo = PersistenceContext.repositories().customers(txCtx);
        this.representativeRepo = PersistenceContext.repositories().representatives(txCtx);
    }

    public RegisterCustomerService(TransactionalContext txCtx, AuthorizationService authz, RegisterShodroneUserService userSvc, CustomerRepository customerRepo, RepresentativeRepository representativeRepo) {
        // dependency injection to become more testable
        this.txCtx = txCtx;
        this.authz = authz;
        this.userSvc = userSvc;
        this.customerRepo = customerRepo;
        this.representativeRepo = representativeRepo;
    }

    public CustomerDTO registerCustomer(final String customerVatNumber, final String customerFirstName, final String customerLastName,
                                        final String street, final String city, final String postalCode, final String country,
                                        final CustomerType customerType, final String representativeFirstName,
                                        final String representativeLastName, final String representativeEmail,
                                        final String representativePhoneNumber, final String representativePosition,
                                        final String representativeShodroneEmail, final String representativePassword) {

        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        txCtx.beginTransaction();

        Customer customer = createCustomer(customerVatNumber, customerFirstName, customerLastName, customerType,
                street, city, postalCode, country);

        ShodroneUser user = createRepresentativeUser(representativeShodroneEmail, representativePassword,
                representativeFirstName, representativeLastName, representativePhoneNumber);

        createRepresentative(customer, representativeEmail, representativePosition, user);

        txCtx.commit();

        return customer.toDTO();
    }

    private Customer createCustomer(final String vatNumber, final String firstName, final String lastName, final CustomerType type,
                                    final String street, final String city, final String postalCode, final String country) {

        Customer customer = new Customer(VAT.valueOf(vatNumber), Name.valueOf(firstName, lastName),
                Address.valueOf(street, city, postalCode, country), CustomerState.CREATED, type);

        return customerRepo.save(customer);
    }

    private ShodroneUser createRepresentativeUser(final String email, final String password,
                                                  final String firstName, final String lastName, final String phoneNumber) {
        final Set<Role> roles = new HashSet<>();
        roles.add(ShodroneRoles.REPRESENTATIVE);

        return userSvc.createShodroneUser(email, password, firstName, lastName, phoneNumber, roles);
    }

    private Representative createRepresentative(final Customer customer, final String email,
                                                final String position, final ShodroneUser user) {

        Representative representative = new Representative(EmailAddress.valueOf(email), customer, user, Designation.valueOf(position));
        return representativeRepo.save(representative);
    }

}

