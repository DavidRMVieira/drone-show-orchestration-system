package lapr4.showproposalmanagement.dto;

import eapli.framework.representations.dto.DTO;

@DTO
public class FigureInShowDTO {

    private String figureCode;
    private double x;
    private double y;
    private double z;

    public FigureInShowDTO(String figureCode, double x, double y, double z) {
        this.figureCode = figureCode;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String figureCode() {
        return figureCode;
    }

    public double Xcoordinate() {
        return x;
    }

    public double Ycoordinate() {
        return y;
    }

    public double Zcoordinate() {
        return z;
    }

}