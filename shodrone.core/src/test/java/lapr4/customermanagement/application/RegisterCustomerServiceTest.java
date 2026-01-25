package lapr4.customermanagement.application;

import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.exceptions.UnauthorizedException;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import lapr4.customermanagement.domain.*;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.customermanagement.util.CustomerTestUtil;
import lapr4.customermanagement.util.RepresentativeTestUtil;
import lapr4.usermanagement.application.RegisterShodroneUserService;
import lapr4.usermanagement.domain.ShodroneRoles;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.customermanagement.repositories.CustomerRepository;
import lapr4.customermanagement.repositories.RepresentativeRepository;
import lapr4.usermanagement.domain.PhoneNumber;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.util.ShodroneUserTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterCustomerServiceTest {

    private RegisterCustomerService subject;
    private CustomerRepository customerRepo;
    private RepresentativeRepository representativeRepo;
    private RegisterShodroneUserService userSvc;
    private AuthorizationService authz;
    private TransactionalContext txCtx;

    private final String customerVatNumber = "AB123456789";
    private final String customerFirstName = "John";
    private final String customerLastName = "Doe";
    private final String street = "Main St";
    private final String city = "SomeCity";
    private final String postalCode = "12345";
    private final String country = "CountryX";
    private final CustomerType customerType = CustomerType.REGULAR;
    private final String representativeFirstName = "Jane";
    private final String representativeLastName = "Smith";
    private final String representativeEmail = "rep@showdrone.com";
    private final String representativePhoneNumber = "987654321";
    private final String representativePosition = "Manager";
    private final String representativeShodroneEmail = "repuser@showdrone.com";
    private final String representativePassword = "Password1";

    @BeforeEach
    void setUp() {
        customerRepo = mock(CustomerRepository.class);
        representativeRepo = mock(RepresentativeRepository.class);
        userSvc = mock(RegisterShodroneUserService.class);
        authz = mock(AuthorizationService.class);
        txCtx = mock(TransactionalContext.class);

        subject = new RegisterCustomerService(txCtx, authz, userSvc, customerRepo, representativeRepo);
    }

    @Test
    void registerCustomerSuccessfully() {
        SystemUser systemUser = ShodroneUserTestUtil.dummyUser(representativeShodroneEmail, representativePassword,
                representativeFirstName, representativeLastName, ShodroneRoles.REPRESENTATIVE);
        ShodroneUser representativeUser = ShodroneUserTestUtil.dummyShodroneUser(ShodroneEmail.valueOf(representativeShodroneEmail),
                PhoneNumber.valueOf(representativePhoneNumber), systemUser);
        Customer customer = CustomerTestUtil.dummyCustomer(customerVatNumber, customerFirstName, customerLastName,
                street, city, postalCode, country, CustomerState.CREATED, customerType);
        Representative representative = RepresentativeTestUtil.dummyRepresentative(representativeEmail, customer,
                representativeUser, representativePosition);

        when(userSvc.createShodroneUser(any(), any(), any(), any(), any(), any())).thenReturn(representativeUser);
        when(customerRepo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(representativeRepo.save(any(Representative.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = subject.registerCustomer(
                customerVatNumber, customerFirstName, customerLastName,
                street, city, postalCode, country,
                customerType, representativeFirstName,
                representativeLastName, representativeEmail,
                representativePhoneNumber, representativePosition,
                representativeShodroneEmail, representativePassword);

        assertNotNull(result);

        verify(customerRepo).save(customer);
        verify(representativeRepo).save(representative);
    }

    @Test
    void registerCustomerReturnsCreatedState() {
        SystemUser systemUser = ShodroneUserTestUtil.dummyUser(representativeShodroneEmail, representativePassword,
                representativeFirstName, representativeLastName, ShodroneRoles.REPRESENTATIVE);
        ShodroneUser representativeUser = ShodroneUserTestUtil.dummyShodroneUser(ShodroneEmail.valueOf(representativeShodroneEmail),
                PhoneNumber.valueOf(representativePhoneNumber), systemUser);

        when(userSvc.createShodroneUser(any(), any(), any(), any(), any(), any())).thenReturn(representativeUser);
        when(customerRepo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(representativeRepo.save(any(Representative.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerDTO result = subject.registerCustomer(
                customerVatNumber, customerFirstName, customerLastName,
                street, city, postalCode, country,
                customerType, representativeFirstName,
                representativeLastName, representativeEmail,
                representativePhoneNumber, representativePosition,
                representativeShodroneEmail, representativePassword);

        assertEquals(CustomerState.CREATED.toString(), result.getState());
    }

    @Test
    void whenCustomerAlreadyExistsThenAnExceptionIsThrown() {
        when(customerRepo.save(any(Customer.class))).thenThrow(new IntegrityViolationException("Duplicate"));

        assertThrows(IntegrityViolationException.class, () -> {
            subject.registerCustomer(
                    customerVatNumber, customerFirstName, customerLastName,
                    street, city, postalCode, country,
                    customerType, representativeFirstName,
                    representativeLastName, representativeEmail,
                    representativePhoneNumber, representativePosition,
                    representativeShodroneEmail, representativePassword
            );
        });
    }

    @Test
    void registerCustomerRequiresAuthorization() {
        doThrow(UnauthorizedException.class).when(authz).ensureAuthenticatedUserHasAnyOf(any());

        assertThrows(UnauthorizedException.class, () -> subject.registerCustomer(
                customerVatNumber, customerFirstName, customerLastName,
                street, city, postalCode, country,
                customerType, representativeFirstName,
                representativeLastName, representativeEmail,
                representativePhoneNumber, representativePosition,
                representativeShodroneEmail, representativePassword
        ));
    }

}
