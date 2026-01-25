package lapr4.figurecategorymanagement.application;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.representations.dto.DTOParser;
import lapr4.figurecategorymanagement.domain.FigureCategory;
import lapr4.figurecategorymanagement.dto.FigureCategoryDTO;
import lapr4.figurecategorymanagement.repositories.FigureCategoryRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FigureCategoryDTOParser implements DTOParser<FigureCategoryDTO, FigureCategory> {

    private final FigureCategoryRepository repository;

    public FigureCategoryDTOParser(final FigureCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public FigureCategory valueOf(final FigureCategoryDTO dto) {
        return repository.ofIdentity(Designation.valueOf(dto.getName()))
                .orElseThrow(() -> new IllegalArgumentException("Unknown figure category: " + dto.getName()));
    }

    public static Iterable<FigureCategoryDTO> transformToDTO(final Iterable<FigureCategory> categories) {
        return StreamSupport.stream(categories.spliterator(), true)
                .map(FigureCategory::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

}
