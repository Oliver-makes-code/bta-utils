package de.olivermakesco.bta_utils.client;

import net.minecraft.client.input.controller.Button;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.input.controller.DigitalPad;
import net.minecraft.client.input.controller.IButtonListener;

public class SDLDigitalPad extends DigitalPad {
    public SDLDigitalPad(ControllerInput controller, Duck_SDL2Controller duck) {
        super(controller, null);
        IButtonListener defaultListener = comp -> comp.getPollData() > 0.5f;
        this.up = new Button("Digital Up", duck.bta_utils$button(SDLComponent.ButtonType.D_UP), defaultListener);
        this.right = new Button("Digital Right", duck.bta_utils$button(SDLComponent.ButtonType.D_RIGHT), defaultListener);
        this.down = new Button("Digital Down", duck.bta_utils$button(SDLComponent.ButtonType.D_DOWN), defaultListener);
        this.left = new Button("Digital Left", duck.bta_utils$button(SDLComponent.ButtonType.D_LEFT), defaultListener);
    }
}
