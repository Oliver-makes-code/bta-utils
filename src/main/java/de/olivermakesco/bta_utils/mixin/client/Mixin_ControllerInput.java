package de.olivermakesco.bta_utils.mixin.client;

import de.olivermakesco.bta_utils.client.BtaUtilsClient;
import de.olivermakesco.bta_utils.client.Duck_SDL2Controller;
import de.olivermakesco.bta_utils.client.SDLComponent;
import de.olivermakesco.bta_utils.client.SDLDigitalPad;
import de.olivermakesco.bta_utils.config.BtaUtilsConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.objectweb.asm.Opcodes;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.controller.*;
import net.minecraft.client.option.FloatOption;
import org.libsdl.SDL_Error;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(value = ControllerInput.class, remap = false)
public abstract class Mixin_ControllerInput {

    @Shadow @Final private Controller controller;

    @Shadow public Joystick joyLeft;

    @Shadow public Joystick joyRight;

    @Shadow @Final public Minecraft minecraft;

    @Shadow public Button buttonZL;

    @Shadow public Button buttonZR;

    @Shadow public Button buttonL;

    @Shadow public Button buttonR;

    @Shadow public DigitalPad digitalPad;

    @Shadow protected abstract Component comp(String id);

    @Inject(
            method = "update",
            at = @At("HEAD")
    )
    private void updateSdlController(CallbackInfo ci) {
        try {
            BtaUtilsClient.MANAGER.pollState();
        } catch (SDL_Error e) {
            e.printStackTrace();
        }
    }

    @Inject(
            method = "setup()V",
            at = @At("HEAD")
    )
    void injectSetupHead(CallbackInfo ci) {
        try {
            BtaUtilsClient.MANAGER.pollState();
        } catch (SDL_Error e) {
            e.printStackTrace();
        }
    }

    @Redirect(
            method = "setup()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/input/controller/ControllerInput;joyLeft:Lnet/minecraft/client/input/controller/Joystick;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    void updateLeftStick(ControllerInput instance, Joystick old) {
        if (!BtaUtilsConfig.useSdl) {
            joyLeft = old;
            return;
        }
        Duck_SDL2Controller duck = (Duck_SDL2Controller) BtaUtilsClient.getController(controller.getPortNumber());
        if (duck == null) {
            joyLeft = old;
            return;
        }
        joyLeft = getJoy(duck, "Left", SDLComponent.AxisType.LX, SDLComponent.AxisType.LY, SDLComponent.ButtonType.LS, minecraft.gameSettings.controllerDeadzoneLeft);
    }

    @Redirect(
            method = "setup()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/input/controller/ControllerInput;joyRight:Lnet/minecraft/client/input/controller/Joystick;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    void updateRightStick(ControllerInput instance, Joystick old) {
        if (!BtaUtilsConfig.useSdl) {
            joyRight = old;
            return;
        }
        Duck_SDL2Controller duck = (Duck_SDL2Controller) BtaUtilsClient.getController(controller.getPortNumber());
        if (duck == null) {
            joyRight = old;
            return;
        }
        joyRight = getJoy(duck, "Right", SDLComponent.AxisType.RX, SDLComponent.AxisType.RY, SDLComponent.ButtonType.RS, minecraft.gameSettings.controllerDeadzoneRight);
    }

    /**
     * This is just a helper to build a Joystick.
     * */
    @Unique
    Joystick getJoy(Duck_SDL2Controller duck, String dir, SDLComponent.AxisType x, SDLComponent.AxisType y, SDLComponent.ButtonType b, FloatOption deadzone) {
        return new Joystick("Analog "+dir, duck.bta_utils$axis(x.ordinal()), duck.bta_utils$axis(y.ordinal()), new Button(dir+" Stick", duck.bta_utils$button(b.ordinal()), c -> c.getPollData() > 0.5), deadzone);
    }

    @Redirect(
            method = "setup()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/input/controller/ControllerInput;digitalPad:Lnet/minecraft/client/input/controller/DigitalPad;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    void updateDigitalPad(ControllerInput instance, DigitalPad old) {
        if (!BtaUtilsConfig.useSdl) {
            digitalPad = old;
            return;
        }
        Duck_SDL2Controller duck = (Duck_SDL2Controller) BtaUtilsClient.getController(controller.getPortNumber());
        if (duck == null) {
            digitalPad = old;
            return;
        }
        digitalPad = new SDLDigitalPad(instance, duck);
    }

    @Redirect(
            method = "setup()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/input/controller/ControllerInput;comp(Ljava/lang/String;)Lnet/java/games/input/Component;"
            )
    )
    Component getSdlComponent(ControllerInput instance, String id) {
        if (!BtaUtilsConfig.useSdl) {
            return comp(id);
        }

        Duck_SDL2Controller duck = (Duck_SDL2Controller) BtaUtilsClient.getController(controller.getPortNumber());

        if (duck == null) {
            return null;
        }

        // Explicit ignores.
        // z is a bit broken, we're setting those in the next method.
        // pov is handled above, and doesn't matter.
        if (Objects.equals(id, "z") || Objects.equals(id, "pov")) {
            return null;
        }

        SDLComponent.ButtonType type = BtaUtilsClient.INPUT_NAMES.get(id);

        if (type == null) {
            return null;
        }

        return duck.bta_utils$button(type);
    }

    /**
     * There's no other easy way to do this.
     * <br>
     * I mean I could <i>probably</i> redirect setters too,
     * or the Button constructor, but that will inflate the amount of methods.
     * */
    @Inject(
            method = "setup()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/input/controller/ControllerInput;digitalPad:Lnet/minecraft/client/input/controller/DigitalPad;",
                    shift = At.Shift.BEFORE
            )
    )
    void overrideTriggers(CallbackInfo ci) {
        if (!BtaUtilsConfig.useSdl) {
            return;
        }

        Duck_SDL2Controller duck = (Duck_SDL2Controller) BtaUtilsClient.getController(controller.getPortNumber());

        if (duck == null) {
            return;
        }

        IButtonListener defaultListener = comp -> comp.getPollData() > 0.5f;

        if (minecraft.gameSettings.controllerSwapShoulderButtons.value) {
            buttonL = new Button("ZL", duck.bta_utils$axis(SDLComponent.AxisType.LZ.ordinal()), defaultListener);
            buttonR = new Button("ZR", duck.bta_utils$axis(SDLComponent.AxisType.RZ.ordinal()), defaultListener);
        } else {
            buttonZL = new Button("ZL", duck.bta_utils$axis(SDLComponent.AxisType.LZ.ordinal()), defaultListener);
            buttonZR = new Button("ZR", duck.bta_utils$axis(SDLComponent.AxisType.RZ.ordinal()), defaultListener);
        }
    }
}
