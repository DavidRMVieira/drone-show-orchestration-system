package lapr4.droneinventory.apllication;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.representations.dto.DTOParser;
import lapr4.droneinventory.domain.DroneModel;
import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.droneinventory.repositories.DroneModelRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DroneModelDTOParser implements DTOParser<DroneModelDTO, DroneModel> {

	private final DroneModelRepository repository;

	public DroneModelDTOParser(final DroneModelRepository repository) {
		this.repository = repository;
	}

	@Override
	public DroneModel valueOf(final DroneModelDTO dto) {
		return repository.ofIdentity(Designation.valueOf(dto.getName()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + dto.getName()));
	}

	public static Iterable<DroneModelDTO> transformToDTO(final Iterable<DroneModel> droneModels) {
		return StreamSupport.stream(droneModels.spliterator(), true)
				.map(DroneModel::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
