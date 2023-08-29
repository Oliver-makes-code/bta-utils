package de.olivermakesco.bta_utils.client;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import net.fabricmc.api.ClientModInitializer;
import org.libsdl.SDL_Error;
import uk.co.electronstudio.sdl2gdx.SDL2Controller;
import uk.co.electronstudio.sdl2gdx.SDL2ControllerManager;

import java.util.HashMap;

public class BtaUtilsClient implements ClientModInitializer {
    public static final HashMap<String, SDLComponent.ButtonType> INPUT_NAMES = new HashMap<>();

    public static final SDL2ControllerManager MANAGER = new SDL2ControllerManager();

    @Override
    public void onInitializeClient() {
        INPUT_NAMES.put("0", SDLComponent.ButtonType.A);
        INPUT_NAMES.put("1", SDLComponent.ButtonType.B);
        INPUT_NAMES.put("2", SDLComponent.ButtonType.X);
        INPUT_NAMES.put("3", SDLComponent.ButtonType.Y);
        INPUT_NAMES.put("4", SDLComponent.ButtonType.LB);
        INPUT_NAMES.put("5", SDLComponent.ButtonType.RB);
        INPUT_NAMES.put("6", SDLComponent.ButtonType.SELECT);
        INPUT_NAMES.put("7", SDLComponent.ButtonType.START);
        try {
            MANAGER.pollState();
        } catch (SDL_Error e) {
            e.printStackTrace();
        }
    }

    public static SDL2Controller getController(int idx) {
        Array<Controller> controllers = MANAGER.getControllers();
        if (0 > idx || idx >= controllers.size) {
            return null;
        }
        return (SDL2Controller) controllers.get(idx);
    }
}
