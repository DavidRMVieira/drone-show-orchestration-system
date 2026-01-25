package lapr4.showproposalmanagement.application;

import eapli.framework.representations.dto.DTOParser;
import lapr4.showproposalmanagement.domain.ShowProposal;
import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import lapr4.showproposalmanagement.repositories.ShowProposalRepository;

import java.util.stream.StreamSupport;

public class ShowProposalDTOParser implements DTOParser<ShowProposalDTO, ShowProposal> {

	private final ShowProposalRepository repository;

	public ShowProposalDTOParser(final ShowProposalRepository repository) {
		this.repository = repository;
	}

	@Override
	public ShowProposal valueOf(final ShowProposalDTO dto) {
		return repository.ofIdentity(dto.id())
				.orElseThrow(() -> new IllegalArgumentException("Unknown show proposal: " + dto.id()));
	}

	public static Iterable<ShowProposalDTO> transformToDTO(final Iterable<ShowProposal> showProposals) {
		return StreamSupport.stream(showProposals.spliterator(), false)
				.map(ShowProposal::toDTO)
				.toList();
	}

}
