package de.olivermakesco.bta_utils.client;

public interface Duck_SDL2Controller {
    SDLComponent bta_utils$axis(int idx);
    SDLComponent bta_utils$button(int idx);

    default SDLComponent bta_utils$axis(SDLComponent.AxisType type) {
        return bta_utils$axis(type.ordinal());
    }

    default SDLComponent bta_utils$button(SDLComponent.ButtonType type) {
        return bta_utils$button(type.ordinal());
    }
}
