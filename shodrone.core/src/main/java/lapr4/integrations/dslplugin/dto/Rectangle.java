package lapr4.integrations.dslplugin.dto;

import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.integrations.sharedkernel.dto.Vector;

public class Rectangle extends GeometricFigure {

    private Vector position;
    private double width;
    private double height;
    private DroneModelDTO droneType;

    public Rectangle(Vector position, double width, double height, DroneModelDTO droneType) {
        super(position, width, droneType);
        this.position = position;
        this.width = width;
        this.height = height;
        this.droneType = droneType;
    }

    @Override
    public String toString() {
        return "Rectangle: " + position + " ; " + width + " ; " + height + " ; " + droneType;
    }

}
