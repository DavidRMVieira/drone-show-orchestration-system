package lapr4.integrations.plugins.dronelanguage.version_0_86;

import lapr4.integrations.dronelanguageplugin.dto.DroneLanguage;
import lapr4.integrations.sharedkernel.dto.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DroneLanguagePluginImplVisitor extends DroneLanguagePluginBaseVisitor<Object> {

    private final DroneLanguage droneLanguage;
    private final Map<String, Object> variables;

    public DroneLanguagePluginImplVisitor() {
        this.droneLanguage = new DroneLanguage();
        this.variables = new HashMap<>();
    }

    @Override
    public Map<String, Object> visitInstancePosition(DroneLanguagePluginParser.InstancePositionContext ctx) {
        String varName = ctx.variableName().getText();
        Vector vector = (Vector) visit(ctx.vectorExpression());
        variables.put(varName, vector);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceArrayPosition(DroneLanguagePluginParser.InstanceArrayPositionContext ctx) {
        String varName = ctx.variableName().getText();
        @SuppressWarnings("unchecked")
        List<Vector> vectors = (List<Vector>) visit(ctx.arrayVectorExpression());
        variables.put(varName, vectors);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceVector(DroneLanguagePluginParser.InstanceVectorContext ctx) {
        String varName = ctx.variableName().getText();
        Vector vector = (Vector) visit(ctx.vectorExpression());
        variables.put(varName, vector);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceLinVelocity(DroneLanguagePluginParser.InstanceLinVelocityContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceAngVelocity(DroneLanguagePluginParser.InstanceAngVelocityContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceDistance(DroneLanguagePluginParser.InstanceDistanceContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceTime(DroneLanguagePluginParser.InstanceTimeContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public DroneLanguage visitInstanceTakeOff(DroneLanguagePluginParser.InstanceTakeOffContext ctx) {
        Double height = (Double) visit(ctx.numberExpression(0));
        Double duration = (Double) visit(ctx.numberExpression(1));
        droneLanguage.addTakeOff(height, duration);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceLand(DroneLanguagePluginParser.InstanceLandContext ctx) {
        Double duration = (Double) visit(ctx.numberExpression());
        droneLanguage.addLand(duration);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceMove1(DroneLanguagePluginParser.InstanceMove1Context ctx) {
        Vector direction = (Vector) visit(ctx.vectorExpression());
        Double speed = (Double) visit(ctx.numberExpression());
        droneLanguage.addMove(direction, speed);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceMove2(DroneLanguagePluginParser.InstanceMove2Context ctx) {
        Vector direction = (Vector) visit(ctx.vectorExpression());
        Double speed = (Double) visit(ctx.numberExpression(0));
        Double time = (Double) visit(ctx.numberExpression(1));
        droneLanguage.addMove(direction, speed, time);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceMovePath(DroneLanguagePluginParser.InstanceMovePathContext ctx) {
        @SuppressWarnings("unchecked")
        List<Vector> path = (List<Vector>) visit(ctx.arrayVectorExpression());
        Double speed = (Double) visit(ctx.numberExpression());
        droneLanguage.addMovePath(path, speed);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceMoveCircle(DroneLanguagePluginParser.InstanceMoveCircleContext ctx) {
        Vector center = (Vector) visit(ctx.vectorExpression());
        Double radius = (Double) visit(ctx.numberExpression(0));
        Double angularVelocity = (Double) visit(ctx.numberExpression(1));
        droneLanguage.addMoveCircle(center, radius, angularVelocity);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceHoover(DroneLanguagePluginParser.InstanceHooverContext ctx) {
        Double duration = (Double) visit(ctx.numberExpression());
        droneLanguage.addHoover(duration);
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceLightsOn(DroneLanguagePluginParser.InstanceLightsOnContext ctx) {
        droneLanguage.addLightsOn();
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceLightOff(DroneLanguagePluginParser.InstanceLightOffContext ctx) {
        droneLanguage.addLightsOff();
        return droneLanguage;
    }

    @Override
    public DroneLanguage visitInstanceBlink(DroneLanguagePluginParser.InstanceBlinkContext ctx) {
        Double frequency = (Double) visit(ctx.numberExpression());
        droneLanguage.addBlink(frequency);
        return droneLanguage;
    }

    @Override
    public Vector visitMulDivVec(DroneLanguagePluginParser.MulDivVecContext ctx) {
        Vector left = (Vector) visit(ctx.vectorExpression(0));
        Vector right = (Vector) visit(ctx.vectorExpression(1));
        int op = ctx.op.getType();
        return performVectorOperation(left, right, op);
    }

    @Override
    public Vector visitAddSubVec(DroneLanguagePluginParser.AddSubVecContext ctx) {
        Vector left = (Vector) visit(ctx.vectorExpression(0));
        Vector right = (Vector) visit(ctx.vectorExpression(1));
        int op = ctx.op.getType();
        return performVectorOperation(left, right, op);
    }

    @Override
    public Vector visitInstanceVectorExp(DroneLanguagePluginParser.InstanceVectorExpContext ctx) {
        return (Vector) visit(ctx.vector());
    }

    @Override
    public Vector visitInstanceVarVector(DroneLanguagePluginParser.InstanceVarVectorContext ctx) {
        String varName = ctx.variableName().getText();
        Object value = variables.get(varName);
        if (value instanceof Vector) {
            return (Vector) value;
        } else {
            throw new IllegalArgumentException("Variable " + varName + " is not a Vector");
        }
    }

    @Override
    public Vector visitVector(DroneLanguagePluginParser.VectorContext ctx) {
        Double x = (Double) visit(ctx.numberExpression(0));
        Double y = (Double) visit(ctx.numberExpression(1));
        Double z = (Double) visit(ctx.numberExpression(2));
        return new Vector(x, y, z);
    }

    @Override
    public Double visitMulDivNum(DroneLanguagePluginParser.MulDivNumContext ctx) {
        Double left = (Double) visit(ctx.numberExpression(0));
        Double right = (Double) visit(ctx.numberExpression(1));
        int op = ctx.op.getType();
        return performNumberOperation(left, right, op);
    }

    @Override
    public Double visitAddSubNum(DroneLanguagePluginParser.AddSubNumContext ctx) {
        Double left = (Double) visit(ctx.numberExpression(0));
        Double right = (Double) visit(ctx.numberExpression(1));
        int op = ctx.op.getType();
        return performNumberOperation(left, right, op);
    }

    @Override
    public Double visitIntanceNumber(DroneLanguagePluginParser.IntanceNumberContext ctx) {
        return (Double) visit(ctx.numberVar());
    }

    @Override
    public Double visitInstanceVarNumber(DroneLanguagePluginParser.InstanceVarNumberContext ctx) {
        String varName = ctx.variableName().getText();
        Object value = variables.get(varName);
        if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Variable " + varName + " is not a number");
        }
    }

    @Override
    public Double visitNumberVar(DroneLanguagePluginParser.NumberVarContext ctx) {
        double value;
        if (ctx.NUMBER() != null) {
            value = Double.parseDouble(ctx.NUMBER().getText());
        } else if( ctx.PI() != null) {
            value = 3.14;
        } else {
            throw new IllegalArgumentException("Unknown number");
        }

        if (ctx.op != null) {
            if (ctx.op.getType() == DroneLanguagePluginLexer.MINUS) {
                value = -value;
            }
        }
        return value;
    }

    @Override
    public List<Vector> visitInstanceArrayVector(DroneLanguagePluginParser.InstanceArrayVectorContext ctx) {
        @SuppressWarnings("unchecked")
        List<Vector> vectors = (List<Vector>) visit(ctx.arrayVector());
        return vectors;
    }

    @Override
    public List<Vector> visitInstanceArrayVarVector(DroneLanguagePluginParser.InstanceArrayVarVectorContext ctx) {
        String varName = ctx.variableName().getText();
        Object value = variables.get(varName);
        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Vector> vectors = (List<Vector>) value;
            return vectors;
        } else {
            throw new IllegalArgumentException("Variable " + varName + " is not a Array Vector");
        }
    }

    @Override
    public List<Vector> visitArrayVector(DroneLanguagePluginParser.ArrayVectorContext ctx) {
        List<Vector> vectors = new ArrayList<>();
        for (DroneLanguagePluginParser.VectorExpressionContext vecCtx : ctx.vectorExpression()) {
            vectors.add((Vector) visit(vecCtx));
        }
        return vectors;
    }

    private Vector performVectorOperation(Vector left, Vector right, int op) {
        switch (op) {
            case DroneLanguagePluginLexer.PLUS:
                return new Vector(left.x() + right.x(), left.y() + right.y(), left.z() + right.z());
            case DroneLanguagePluginLexer.MINUS:
                return new Vector(left.x() - right.x(), left.y() - right.y(), left.z() - right.z());
            case DroneLanguagePluginLexer.MULT:
                return new Vector(left.x() * right.x(), left.y() * right.y(), left.z() * right.z());
            case DroneLanguagePluginLexer.DIV:
                return new Vector(left.x() / right.x(), left.y() / right.y(), left.z() / right.z());
            default:
                throw new IllegalArgumentException("Unknown vector operator: " + op);
        }
    }

    private Double performNumberOperation(Double left, Double right, int op) {
        switch (op) {
            case DroneLanguagePluginLexer.PLUS:
                return left + right;
            case DroneLanguagePluginLexer.MINUS:
                return left - right;
            case DroneLanguagePluginLexer.MULT:
                return left * right;
            case DroneLanguagePluginLexer.DIV:
                return left / right;
            default:
                throw new IllegalArgumentException("Unknown number operator: " + op);
        }
    }

    public DroneLanguage droneLanguagePlugin() {
        return droneLanguage;
    }

}