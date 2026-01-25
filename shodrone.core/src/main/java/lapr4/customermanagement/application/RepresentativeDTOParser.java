package lapr4.customermanagement.application;

import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.representations.dto.DTOParser;
import lapr4.customermanagement.domain.Representative;
import lapr4.customermanagement.dto.RepresentativeDTO;
import lapr4.customermanagement.repositories.RepresentativeRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RepresentativeDTOParser implements DTOParser<RepresentativeDTO, Representative> {

	private final RepresentativeRepository repository;

	public RepresentativeDTOParser(final RepresentativeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Representative valueOf(final RepresentativeDTO dto) {
		return repository.ofIdentity(EmailAddress.valueOf(dto.getEmail()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown representative: " + dto.getEmail()));
	}

	public static Iterable<RepresentativeDTO> transformToDTO(final Iterable<Representative> representatives) {
		return StreamSupport.stream(representatives.spliterator(), true)
				.map(Representative::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
