package de.olivermakesco.bta_utils.client;

import net.java.games.input.Component;
import uk.co.electronstudio.sdl2gdx.SDL2Controller;

public class SDLComponent implements Component {
    public final SDL2Controller controller;
    public final int idx;
    public final Type type;

    public SDLComponent(SDL2Controller controller, int idx, Type type) {
        this.controller = controller;
        this.idx = idx;
        this.type = type;
    }

    @Override
    public Identifier getIdentifier() {
        return type.getIdentifier(this);
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    public boolean isAnalog() {
        return type == Type.AXIS;
    }

    @Override
    public float getDeadZone() {
        return 0.4f;
    }

    @Override
    public float getPollData() {
        return type.getPollData(this);
    }

    @Override
    public String getName() {
        return type.getString(this);
    }

    public enum Type {
        AXIS,
        BUTTON;

        @Override
        public String toString() {
            return this == AXIS ? "AXIS" : "BUTTON";
        }

        public String getString(SDLComponent component) {
            return this + "_" + component.idx;
        }

        public float getPollData(SDLComponent component) {
            if (this == AXIS) {
                if (component.idx == AxisType.LR.ordinal()) {
                    float v = component.controller.getAxis(component.idx);
                    System.out.println(v);
                    return v;
                }
                return component.controller.getAxis(component.idx);
            }
            return component.controller.getButton(component.idx) ? 1 : 0;
        }

        public Identifier getIdentifier(SDLComponent component) {
            if (this == AXIS) {
                return AxisType.values()[component.idx].getIdentifier();
            }
            return ButtonType.values()[component.idx].getIdentifier();
        }
    }

    public enum ButtonType {
        A, B, X, Y, SELECT, MENU, START, LS, RS, LB, RB, D_UP, D_DOWN, D_LEFT, D_RIGHT, MAX;
        public Identifier getIdentifier() {
            switch (this) {
                case A:
                    return Identifier.Button.A;
                case B:
                    return Identifier.Button.B;
                case X:
                    return Identifier.Button.X;
                case Y:
                    return Identifier.Button.Y;
                case LB:
                    return Identifier.Button.LEFT_THUMB;
                case RB:
                    return Identifier.Button.RIGHT_THUMB;
                case SELECT:
                    return Identifier.Button.SELECT;
                case MENU:
                    return Identifier.Button.MODE;
                case LS:
                    return Identifier.Button.LEFT_THUMB3;
                case RS:
                    return Identifier.Button.RIGHT_THUMB3;
                default:
                    return Identifier.Button.UNKNOWN;
            }
        }
    }

    public enum AxisType {
        LX, LY, RX, RY, LZ, RZ, LR;
        public Identifier getIdentifier() {
            switch (this) {
                case LX:
                    return Identifier.Axis.X;
                case LY:
                    return Identifier.Axis.Y;
                case LZ:
                    return Identifier.Axis.Z;
                case RX:
                    return Identifier.Axis.RX;
                case RY:
                    return Identifier.Axis.RY;
                case RZ:
                    return Identifier.Axis.RZ;
                case LR:
                    return Identifier.Axis.SLIDER;
                default:
                    return Identifier.Axis.UNKNOWN;
            }
        }
    }
}
