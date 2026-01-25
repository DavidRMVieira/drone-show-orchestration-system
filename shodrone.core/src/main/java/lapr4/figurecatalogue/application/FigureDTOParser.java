package lapr4.figurecatalogue.application;

import eapli.framework.representations.dto.DTOParser;
import lapr4.figurecatalogue.domain.Figure;
import lapr4.figurecatalogue.domain.FigureCode;
import lapr4.figurecatalogue.dto.FigureDTO;
import lapr4.figurecatalogue.repositories.Catalogue;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FigureDTOParser implements DTOParser<FigureDTO, Figure> {

    private final Catalogue repository;

    public FigureDTOParser(final Catalogue repository) {
        this.repository = repository;
    }

    @Override
    public Figure valueOf(final FigureDTO dto) {
        return repository.ofIdentity(FigureCode.valueOf(dto.getCode()))
                .orElseThrow(() -> new IllegalArgumentException("Unknown figure: " + dto.getCode()));
    }

    public static Iterable<FigureDTO> transformToDTO(final Iterable<Figure> figures) {
        return StreamSupport.stream(figures.spliterator(), true)
                .map(Figure::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

}