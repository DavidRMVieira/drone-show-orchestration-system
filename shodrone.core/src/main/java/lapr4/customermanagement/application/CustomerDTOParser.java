package lapr4.customermanagement.application;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.money.domain.model.Money;
import eapli.framework.representations.dto.DTOParser;
import lapr4.customermanagement.domain.Customer;
import lapr4.customermanagement.domain.VAT;
import lapr4.customermanagement.dto.CustomerDTO;
import lapr4.customermanagement.repositories.CustomerRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomerDTOParser implements DTOParser<CustomerDTO, Customer> {

	private final CustomerRepository repository;

	public CustomerDTOParser(final CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public Customer valueOf(final CustomerDTO dto) {
		return repository.ofIdentity(VAT.valueOf(dto.getVatNumber()))
				.orElseThrow(() -> new IllegalArgumentException("Unknown customer: " + dto.getVatNumber()));
	}

	public static Iterable<CustomerDTO> transformToDTO(final Iterable<Customer> customers) {
		return StreamSupport.stream(customers.spliterator(), true)
				.map(Customer::toDTO)
				.collect(Collectors.toUnmodifiableList());
	}

}
