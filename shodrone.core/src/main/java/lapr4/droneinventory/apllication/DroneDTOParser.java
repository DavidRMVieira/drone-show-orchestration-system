package lapr4.droneinventory.apllication;

import eapli.framework.representations.dto.DTOParser;
import lapr4.droneinventory.domain.Drone;
import lapr4.droneinventory.domain.DroneSerialNumber;
import lapr4.droneinventory.dto.DroneDTO;
import lapr4.droneinventory.repositories.DroneRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DroneDTOParser implements DTOParser<DroneDTO, Drone> {

	private final DroneRepository repository;

	public DroneDTOParser(final DroneRepository repository) {
		this.repository = repository;
	}

	@Override
	public Drone valueOf(final DroneDTO dto) {
		return repository.ofIdentity(DroneSerialNumber.valueOf(dto.getSerialNumber()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown drone: " + dto.getSerialNumber()));
	}

	public static Iterable<DroneDTO> transformToDTO(final Iterable<Drone> drones) {
		return StreamSupport.stream(drones.spliterator(), true)
				.map(Drone::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
