package lapr4.integrations.dronelanguageplugin.dto;

public enum InstructionType {

    TAKEOFF,
    LAND,
    MOVE,
    MOVEPATH,
    MOVECIRCLE,
    HOOVER,
    LIGHTSON,
    LIGHTSOFF,
    BLINK;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}