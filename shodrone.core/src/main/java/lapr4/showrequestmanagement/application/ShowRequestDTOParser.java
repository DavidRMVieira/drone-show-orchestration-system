package lapr4.showrequestmanagement.application;

import eapli.framework.representations.dto.DTOParser;
import lapr4.showrequestmanagement.domain.ShowRequest;
import lapr4.showrequestmanagement.dto.ShowRequestDTO;
import lapr4.showrequestmanagement.repositories.ShowRequestRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShowRequestDTOParser implements DTOParser<ShowRequestDTO, ShowRequest> {

	private final ShowRequestRepository repository;

	public ShowRequestDTOParser(final ShowRequestRepository repository) {
		this.repository = repository;
	}

	@Override
	public ShowRequest valueOf(final ShowRequestDTO dto) {
		return repository.ofIdentity(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("Unknown show request: " + dto.getId()));
	}

	public static Iterable<ShowRequestDTO> transformToDTO(final Iterable<ShowRequest> showRequests) {
		return StreamSupport.stream(showRequests.spliterator(), true)
				.map(ShowRequest::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
