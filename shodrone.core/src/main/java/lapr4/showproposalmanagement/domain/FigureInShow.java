package lapr4.showproposalmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.representations.dto.DTOable;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import lapr4.showproposalmanagement.dto.FigureInShowDTO;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Embeddable
@EqualsAndHashCode
public class FigureInShow implements ValueObject, DTOable<FigureInShowDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    private String figureCode;

    private double coordinateX;
    private double coordinateY;
    private double coordinateZ;

    public FigureInShow(final String figureCode, final double coordinateX, final double coordinateY, final double coordinateZ) {
        Preconditions.noneNull(figureCode);
        this.figureCode = figureCode;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateZ = coordinateZ;
    }

    protected FigureInShow() {
        // for ORM
    }

    public static FigureInShow valueOf(final String figureCode, final double coordinateX, final double coordinateY, final double coordinateZ) {
        return new FigureInShow(figureCode, coordinateX, coordinateY, coordinateZ);
    }

    public String figureCode() {
        return figureCode;
    }

    public double coordinateX() {
        return coordinateX;
    }

    public double coordinateY() {
        return coordinateY;
    }

    public double coordinateZ() {
        return coordinateZ;
    }

    @Override
    public FigureInShowDTO toDTO() {
        return new FigureInShowDTO(figureCode, coordinateX, coordinateY, coordinateZ);
    }

    @Override
    public String toString() {
        return "FigureInShow{" +
                "figureCode='" + figureCode + '\'' +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", coordinateZ=" + coordinateZ +
                '}';
    }
}
