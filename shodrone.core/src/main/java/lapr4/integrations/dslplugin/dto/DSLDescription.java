package lapr4.integrations.dslplugin.dto;

import lapr4.integrations.sharedkernel.dto.Vector;

import java.util.LinkedHashMap;
import java.util.Map;

public class DSLDescription {

    Map<Method, GeometricFigure> instructions = new LinkedHashMap<>();

    public void addMove(Vector direction, double distance, double velocity, GeometricFigure figure) {
        Method move = Method.move(direction, distance, velocity);
        instructions.put(move, figure);
    }

    public void addMovePos(Vector direction, double velocity, GeometricFigure figure) {
        Method movePos = Method.movePos(direction, velocity);
        instructions.put(movePos, figure);
    }

    public void addRotate(Vector vector1, Vector vector2, double distance, double velocity, GeometricFigure figure) {
        Method rotate = Method.rotate(vector1, vector2, distance, velocity);
        instructions.put(rotate, figure);
    }

    public void addLightsOn(String color, GeometricFigure figure) {
        Method lightsOn = Method.lightsOn(color);
        instructions.put(lightsOn, figure);
    }

    public void addLightsOf(GeometricFigure figure) {
        Method lightsOf = Method.lightsOf();
        instructions.put(lightsOf, figure);
    }

    public void addPause(double duration) {
        Method pause = Method.pause(duration);
        instructions.put(pause, null);
    }

    public Map<Method, GeometricFigure> instructions() {
        return instructions;
    }

}
