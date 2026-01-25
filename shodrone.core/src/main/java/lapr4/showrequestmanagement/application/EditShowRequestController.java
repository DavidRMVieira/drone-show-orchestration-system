package lapr4.showrequestmanagement.application;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import lapr4.infrastructure.persistence.PersistenceContext;
import lapr4.showrequestmanagement.domain.Duration;
import lapr4.showrequestmanagement.domain.Place;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.domain.ShowRequestState;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;
import lapr4.usermanagement.domain.ShodroneRoles;

import java.util.Date;

public class EditShowRequestController {

    private final AuthorizationService authz;
    private final ShowRequestRepository showRequestRepository;

    public EditShowRequestController() {
        this.authz = AuthzRegistry.authorizationService();
        this.showRequestRepository = PersistenceContext.repositories().showRequests();
    }

    public ShowRequestDTO changeShowRequestPlace(final String newPlace, ShowRequestDTO showRequestDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(showRequestDTO);
        showRequest.changePlaceTo(Place.valueOf(newPlace));
        return showRequestRepository.save(showRequest).toDTO();
    }

    public ShowRequestDTO changeShowRequestDate(final Date newDate, ShowRequestDTO showRequestDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(showRequestDTO);
        showRequest.changeDateTo(newDate);
        return showRequestRepository.save(showRequest).toDTO();
    }

    public ShowRequestDTO changeShowRequestDuration(final int newDuration, ShowRequestDTO showRequestDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(showRequestDTO);
        showRequest.changeDurationTo(Duration.valueOf(newDuration));
        return showRequestRepository.save(showRequest).toDTO();
    }

    public ShowRequestDTO changeShowRequestState(final ShowRequestState newState, ShowRequestDTO showRequestDTO) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR);

        ShowRequest showRequest = new ShowRequestDTOParser(showRequestRepository).valueOf(showRequestDTO);
        showRequest.changeStateTo(newState);
        return showRequestRepository.save(showRequest).toDTO();
    }

    public Iterable<ShowRequestDTO> allShowRequests() {
        final Iterable<ShowRequest> showRequests = showRequestRepository.findAll();
        return ShowRequestDTOParser.transformToDTO(showRequests);
    }
}
