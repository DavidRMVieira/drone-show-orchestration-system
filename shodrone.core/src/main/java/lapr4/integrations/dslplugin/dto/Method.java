package lapr4.integrations.dslplugin.dto;

import lapr4.integrations.sharedkernel.dto.Vector;

public class Method {

    private MethodType type;
    private double number1;
    private double number2;
    private String color;
    private Vector vector1;
    private Vector vector2;

    public static Method move(Vector direction, double distance, double velocity) {
        Method m = new Method();
        m.type = MethodType.MOVE;
        m.vector1 = direction;
        m.number1 = distance;
        m.number2 = velocity;
        return m;
    }

    public static Method movePos(Vector direction, double velocity) {
        Method m = new Method();
        m.type = MethodType.MOVEPOS;
        m.vector1 = direction;
        m.number1 = velocity;
        return m;
    }

    public static Method rotate(Vector vector1, Vector vector2, double distance, double velocity) {
        Method m = new Method();
        m.type = MethodType.ROTATE;
        m.vector1 = vector1;
        m.vector2 = vector2;
        m.number1 = distance;
        m.number2 = velocity;
        return m;
    }

    public static Method lightsOn(String color) {
        Method m = new Method();
        m.type = MethodType.LIGHTSON;
        m.color = color;
        return m;
    }

    public static Method lightsOf() {
        Method m = new Method();
        m.type = MethodType.LIGHTSOFF;
        return m;
    }

    public static Method pause(double duration) {
        Method m = new Method();
        m.type = MethodType.PAUSE;
        m.number1 = duration;
        return m;
    }

    @Override
    public String toString() {
        switch (type) {
            case MOVE:
                return type + " : " + vector1 + " ; " + number1 + " ; " + number2;
            case MOVEPOS:
                return type + " : " + vector1 + " ; " + number1;
            case ROTATE:
                return type + " : " + vector1 + " ; " + vector2 + " ; " + number1 + " ; " + number2;
            case LIGHTSON:
                return type + " : " + color;
            case LIGHTSOFF:
                return type.toString();
            case PAUSE:
                return type + " : " + number1;
            default:
                return type + " :";
        }
    }


}
