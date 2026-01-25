package lapr4.integrations.dronelanguageplugin.dto;

import lapr4.integrations.sharedkernel.dto.Vector;

import java.util.List;

public class Instruction {

    private InstructionType type;
    private double number1;
    private double number2;
    private String color;
    private Vector vector;
    private List<Vector> vectors;

    public static Instruction createLand(double duration) {
        Instruction i = new Instruction();
        i.type = InstructionType.LAND;
        i.number1 = duration;
        return i;
    }

    public static Instruction createHoover(double duration) {
        Instruction i = new Instruction();
        i.type = InstructionType.HOOVER;
        i.number1 = duration;
        return i;
    }

    public static Instruction createBlink(double duration) {
        Instruction i = new Instruction();
        i.type = InstructionType.BLINK;
        i.number1 = duration;
        return i;
    }

    public static Instruction createMoveCircle(Vector center, double radius, double speed) {
        Instruction i = new Instruction();
        i.type = InstructionType.MOVECIRCLE;
        i.vector = center;
        i.number1 = radius;
        i.number2 = speed;
        return i;
    }

    public static Instruction createLightsOn(String color) {
        Instruction i = new Instruction();
        i.type = InstructionType.LIGHTSON;
        i.color = color;
        return i;
    }

    public static Instruction createLightsOn() {
        Instruction i = new Instruction();
        i.type = InstructionType.LIGHTSON;
        return i;
    }

    public static Instruction createLightsOff() {
        Instruction i = new Instruction();
        i.type = InstructionType.LIGHTSOFF;
        return i;
    }

    public static Instruction createTakeOff(double height, double duration) {
        Instruction i = new Instruction();
        i.type = InstructionType.TAKEOFF;
        i.number1 = height;
        i.number2 = duration;
        return i;
    }

    public static Instruction createMove(Vector direction, double speed, double duration) {
        Instruction i = new Instruction();
        i.type = InstructionType.MOVE;
        i.vector = direction;
        i.number1 = speed;
        i.number2 = duration;
        return i;
    }

    public static Instruction createMove(Vector direction, double speed) {
        Instruction i = new Instruction();
        i.type = InstructionType.MOVE;
        i.vector = direction;
        i.number1 = speed;
        return i;
    }

    public static Instruction createMovePath(List<Vector> path, double speed) {
        Instruction i = new Instruction();
        i.type = InstructionType.MOVEPATH;
        i.vectors = path;
        i.number1 = speed;
        return i;
    }

    @Override
    public String toString() {
        switch (type) {
            case TAKEOFF:
                return type + " : " + number1 + " ; " + number2;
            case LAND:
            case HOOVER:
            case BLINK:
                return type + " : " + number1;
            case MOVECIRCLE:
                return type + " : " + vector + " ; " + number1 + " ; " + number2;
            case LIGHTSON:
                return type + " : " + (color != null ? color : "");
            case LIGHTSOFF:
                return type + " :";
            case MOVE:
                return number2 != 0
                        ? type + " : " + vector + " ; " + number1 + " ; " + number2
                        : type + " : " + vector + " ; " + number1;
            case MOVEPATH:
                return type + " : " + vectors + " ; " + number1;
            default:
                return type + " :";
        }
    }

}
