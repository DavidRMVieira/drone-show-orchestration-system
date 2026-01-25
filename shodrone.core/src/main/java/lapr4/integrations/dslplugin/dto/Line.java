package lapr4.integrations.dslplugin.dto;

import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.integrations.sharedkernel.dto.Vector;

public class Line extends GeometricFigure {

    private Vector position;
    private double width;
    private DroneModelDTO droneType;

    public Line(Vector position, double width, DroneModelDTO droneType) {
        super(position, width, droneType);
        this.position = position;
        this.width = width;
        this.droneType = droneType;
    }

    @Override
    public String toString() {
        return "Line: " + position + " ; " + width + " ; " + droneType;
    }

}
