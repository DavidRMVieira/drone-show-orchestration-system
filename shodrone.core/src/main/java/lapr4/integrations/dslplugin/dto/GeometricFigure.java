package lapr4.integrations.dslplugin.dto;

import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.integrations.sharedkernel.dto.Vector;

public abstract class GeometricFigure {

    private Vector position;
    private double width;
    private DroneModelDTO droneType;

    public GeometricFigure(Vector position, double width, DroneModelDTO droneType) {
        this.position = position;
        this.width = width;
        this.droneType = droneType;
    }

}