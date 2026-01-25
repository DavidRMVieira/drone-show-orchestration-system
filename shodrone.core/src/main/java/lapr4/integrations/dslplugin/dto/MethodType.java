package lapr4.integrations.dslplugin.dto;

public enum MethodType {

    PAUSE,
    MOVE,
    MOVEPOS,
    ROTATE,
    LIGHTSON,
    LIGHTSOFF;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}