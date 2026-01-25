package lapr4.integrations.plugins.dsl.version_1_1_23;

import lapr4.droneinventory.dto.DroneModelDTO;
import lapr4.integrations.sharedkernel.dto.Vector;
import lapr4.integrations.dslplugin.dto.*;

import java.util.HashMap;
import java.util.Map;

public class DSLPluginImplVisitor extends DSLPluginBaseVisitor<Object> {

    private final Map<String, Object> variables = new HashMap<>();
    private GeometricFigure currentGeometricFigure;
    private final DSLDescription dsl = new DSLDescription();

    @Override
    public Map<String, Object> visitDroneType(DSLPluginParser.DroneTypeContext ctx) {
        String droneTypeName = ctx.variableName().getText();
        DroneModelDTO droneType = new DroneModelDTO(droneTypeName);
        variables.put(droneTypeName, droneType);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstancePosition(DSLPluginParser.InstancePositionContext ctx) {
        String varName = ctx.variableName().getText();
        Vector vector = (Vector) visit(ctx.vectorExpression());
        variables.put(varName, vector);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceVelocity(DSLPluginParser.InstanceVelocityContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceDistance(DSLPluginParser.InstanceDistanceContext ctx) {
        String varName = ctx.variableName().getText();
        Double value = (Double) visit(ctx.numberExpression());
        variables.put(varName, value);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceLine(DSLPluginParser.InstanceLineContext ctx) {
        String varName = ctx.variableName(0).getText();
        Vector vector = (Vector) visit(ctx.vectorExpression());
        Double length = (Double) visit(ctx.numberExpression());
        String droneVarName = ctx.variableName(1).getText();
        Object drone = variables.get(droneVarName);
        if (!(drone instanceof DroneModelDTO)) {
            throw new IllegalArgumentException("Variable " + varName + " is not a Drone");
        }
        Line line = new Line(vector, length, (DroneModelDTO) drone);
        variables.put(varName, line);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceRectangle(DSLPluginParser.InstanceRectangleContext ctx) {
        String varName = ctx.variableName(0).getText();
        Vector vector = (Vector) visit(ctx.vectorExpression());
        Double width = (Double) visit(ctx.numberExpression(0));
        Double height = (Double) visit(ctx.numberExpression(1));
        String droneVarName = ctx.variableName(1).getText();
        Object drone = variables.get(droneVarName);
        if (!(drone instanceof DroneModelDTO)) {
            throw new IllegalArgumentException("Variable " + varName + " is not a Drone");
        }
        Rectangle rectangle = new Rectangle(vector, width, height, (DroneModelDTO) drone);
        variables.put(varName, rectangle);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceCircle(DSLPluginParser.InstanceCircleContext ctx) {
        String varName = ctx.variableName(0).getText();
        Vector center = (Vector) visit(ctx.vectorExpression());
        Double radius = (Double) visit(ctx.numberExpression());
        String droneVarName = ctx.variableName(1).getText();
        Object drone = variables.get(droneVarName);
        if (!(drone instanceof DroneModelDTO)) {
            throw new IllegalArgumentException("Variable " + varName + " is not a Drone");
        }
        Circle circle = new Circle(center, radius, (DroneModelDTO) drone);
        variables.put(varName, circle);
        return variables;
    }

    @Override
    public Map<String, Object> visitInstanceCircumference(DSLPluginParser.InstanceCircumferenceContext ctx) {
        String varName = ctx.variableName(0).getText();
        Vector center = (Vector) visit(ctx.vectorExpression());
        Double radius = (Double) visit(ctx.numberExpression());
        String droneVarName = ctx.variableName(1).getText();
        Object drone = variables.get(droneVarName);
        if (!(drone instanceof DroneModelDTO)) {
            throw new IllegalArgumentException("Variable " + varName + " is not a Drone");
        }
        Circumference circumference = new Circumference(center, radius, (DroneModelDTO) drone);
        variables.put(varName, circumference);
        return variables;
    }

    @Override
    public DSLDescription visitPauseStmt(DSLPluginParser.PauseStmtContext ctx) {
        double duration = Double.parseDouble(ctx.NUMBER().getText());
        dsl.addPause(duration);
        return dsl;
    }

    public Object visitCommandStmt(DSLPluginParser.CommandStmtContext ctx) {
        String figureName = ctx.variableName().getText();
        Object figure = variables.get(figureName);
        if (!(figure instanceof GeometricFigure)) {
            throw new IllegalArgumentException("Variable " + figureName + " is not a Geometric Figure");
        }
        currentGeometricFigure = (GeometricFigure) figure;
        Object result = visit(ctx.methodCall());
        currentGeometricFigure = null;
        return result;
    }

    @Override
    public DSLDescription visitInstanceMove(DSLPluginParser.InstanceMoveContext ctx) {
        Vector direction = (Vector) visit(ctx.vectorExpression());
        Double speed = (Double) visit(ctx.numberExpression(0));
        Double time = (Double) visit(ctx.numberExpression(1));
        dsl.addMove(direction, speed, time, currentGeometricFigure);
        return dsl;
    }

    @Override
    public DSLDescription visitInstanceMovePos(DSLPluginParser.InstanceMovePosContext ctx) {
        Vector position = (Vector) visit(ctx.vectorExpression());
        Double speed = (Double) visit(ctx.numberExpression());
        dsl.addMovePos(position, speed, currentGeometricFigure);
        return dsl;
    }

    @Override
    public DSLDescription visitInstanceRotate(DSLPluginParser.InstanceRotateContext ctx) {
        Vector axis = (Vector) visit(ctx.vectorExpression(0));
        Vector center = (Vector) visit(ctx.vectorExpression(1));
        Double angularVelocity = (Double) visit(ctx.numberExpression(0));
        Double time = (Double) visit(ctx.numberExpression(1));
        dsl.addRotate(axis, center, angularVelocity, time, currentGeometricFigure);
        return dsl;
    }

    @Override
    public DSLDescription visitInstanceLightsOn(DSLPluginParser.InstanceLightsOnContext ctx) {
        String color = ctx.COLORNAME().getText();
        dsl.addLightsOn(color, currentGeometricFigure);
        return dsl;
    }

    @Override
    public DSLDescription visitInstanceLightsOff(DSLPluginParser.InstanceLightsOffContext ctx) {
        dsl.addLightsOf(currentGeometricFigure);
        return dsl;
    }

    @Override
    public Vector visitMulDivVec(DSLPluginParser.MulDivVecContext ctx) {
        Vector left = (Vector) visit(ctx.vectorExpression(0));
        Vector right = (Vector) visit(ctx.vectorExpression(1));
        int op = ctx.op.getType();
        return performVectorOperation(left, right, op);
    }

    @Override
    public Vector visitAddSubVec(DSLPluginParser.AddSubVecContext ctx) {
        Vector left = (Vector) visit(ctx.vectorExpression(0));
        Vector right = (Vector) visit(ctx.vectorExpression(1));
        int op = ctx.op.getType();
        return performVectorOperation(left, right, op);
    }

    @Override
    public Vector visitInstanceVectorExp(DSLPluginParser.InstanceVectorExpContext ctx) {
        return (Vector) visit(ctx.vector());
    }

    @Override
    public Vector visitInstanceVarVector(DSLPluginParser.InstanceVarVectorContext ctx) {
        String varName = ctx.variableName().getText();
        Object value = variables.get(varName);
        if (value instanceof Vector) {
            return (Vector) value;
        }
        throw new IllegalArgumentException("Variable " + varName + " is not a Vector");
    }

    @Override
    public Vector visitVector(DSLPluginParser.VectorContext ctx) {
        Double x = (Double) visit(ctx.numberExpression(0));
        Double y = (Double) visit(ctx.numberExpression(1));
        Double z = (Double) visit(ctx.numberExpression(2));
        return new Vector(x, y, z);
    }

    @Override
    public Double visitMulDivNum(DSLPluginParser.MulDivNumContext ctx) {
        Double left = (Double) visit(ctx.numberExpression(0));
        Double right = (Double) visit(ctx.numberExpression(1));
        int op = ctx.op.getType();
        return performNumberOperation(left, right, op);
    }

    @Override
    public Double visitAddSubNum(DSLPluginParser.AddSubNumContext ctx) {
        Double left = (Double) visit(ctx.numberExpression(0));
        Double right = (Double) visit(ctx.numberExpression(1));
        int op = ctx.op.getType();
        return performNumberOperation(left, right, op);
    }

    @Override
    public Double visitIntanceNumber(DSLPluginParser.IntanceNumberContext ctx) {
        return (Double) visit(ctx.numberVar());
    }

    @Override
    public Double visitInstanceVarNumber(DSLPluginParser.InstanceVarNumberContext ctx) {
        String varName = ctx.variableName().getText();
        Object value = variables.get(varName);
        if (value instanceof Double) {
            return (Double) value;
        }
        throw new IllegalArgumentException("Variable " + varName + " is not a number");
    }

    @Override
    public Double visitNumberVar(DSLPluginParser.NumberVarContext ctx) {
        double value;
        if (ctx.NUMBER() != null) {
            value = Double.parseDouble(ctx.NUMBER().getText());
        } else if( ctx.PI() != null) {
            value = 3.14;
        } else {
            throw new IllegalArgumentException("Unknown number");
        }

        if (ctx.op != null) {
            if (ctx.op.getType() == DSLPluginLexer.MINUS) {
                value = -value;
            }
        }
        return value;
    }

    private Vector performVectorOperation(Vector left, Vector right, int op) {
        switch (op) {
            case DSLPluginLexer.PLUS:
                return new Vector(left.x() + right.x(), left.y() + right.y(), left.z() + right.z());
            case DSLPluginLexer.MINUS:
                return new Vector(left.x() - right.x(), left.y() - right.y(), left.z() - right.z());
            case DSLPluginLexer.MULT:
                return new Vector(left.x() * right.x(), left.y() * right.y(), left.z() * right.z());
            case DSLPluginLexer.DIV:
                return new Vector(left.x() / right.x(), left.y() / right.y(), left.z() / right.z());
            default:
                throw new IllegalArgumentException("Unknown vector operator: " + op);
        }
    }

    private Double performNumberOperation(Double left, Double right, int op) {
        switch (op) {
            case DSLPluginLexer.PLUS:
                return left + right;
            case DSLPluginLexer.MINUS:
                return left - right;
            case DSLPluginLexer.MULT:
                return left * right;
            case DSLPluginLexer.DIV:
                return left / right;
            default: throw new IllegalArgumentException("Unknown number operator: " + op);
        }
    }

    public DSLDescription DSLPlugin() {
        return dsl;
    }

}