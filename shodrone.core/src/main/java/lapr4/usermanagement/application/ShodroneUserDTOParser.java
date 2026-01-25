package lapr4.usermanagement.application;

import eapli.framework.representations.dto.DTOParser;
import lapr4.usermanagement.domain.ShodroneEmail;
import lapr4.usermanagement.domain.ShodroneUser;
import lapr4.usermanagement.dto.ShodroneUserDTO;
import lapr4.usermanagement.repositories.ShodroneUserRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShodroneUserDTOParser implements DTOParser<ShodroneUserDTO, ShodroneUser> {

	private final ShodroneUserRepository repository;

	public ShodroneUserDTOParser(final ShodroneUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public ShodroneUser valueOf(final ShodroneUserDTO dto) {
		return repository.ofIdentity(ShodroneEmail.valueOf(dto.getEmail()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown user: " + dto.getEmail()));
	}

	public static Iterable<ShodroneUserDTO> transformToDTO(final Iterable<ShodroneUser> users) {
		return StreamSupport.stream(users.spliterator(), true)
				.map(ShodroneUser::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
