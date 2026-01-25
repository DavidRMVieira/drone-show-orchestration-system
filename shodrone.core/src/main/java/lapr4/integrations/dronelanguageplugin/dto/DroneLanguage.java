package lapr4.integrations.dronelanguageplugin.dto;

import lapr4.integrations.sharedkernel.dto.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DroneLanguage {

    private final List<Instruction> instructions = new ArrayList<>();

    public void addTakeOff(double height, double duration) {
        instructions.add(Instruction.createTakeOff(height, duration));
    }

    public void addLand(double duration) {
        instructions.add(Instruction.createLand(duration));
    }

    public void addMove(Vector direction, double speed) {
        instructions.add(Instruction.createMove(direction, speed));
    }

    public void addMove(Vector direction, double speed, double time) {
        instructions.add(Instruction.createMove(direction, speed, time));
    }

    public void addMovePath(List<Vector> path, double speed) {
        instructions.add(Instruction.createMovePath(path, speed));
    }

    public void addMoveCircle(Vector center, double radius, double angularVelocity) {
        instructions.add(Instruction.createMoveCircle(center, radius, angularVelocity));
    }

    public void addHoover(double duration) {
        instructions.add(Instruction.createHoover(duration));
    }

    public void addLightsOn(String color) {
        instructions.add(Instruction.createLightsOn(color));
    }

    public void addLightsOn() {
        instructions.add(Instruction.createLightsOn());
    }

    public void addLightsOff() {
        instructions.add(Instruction.createLightsOff());
    }

    public void addBlink(double frequency) {
        instructions.add(Instruction.createBlink(frequency));
    }

    public List<Instruction> instructions() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public String toString() {
        return "DroneLanguage{" +
                "instructions=" + instructions +
                '}';
    }
}
