package de.olivermakesco.bta_utils.mixin.client;

import de.olivermakesco.bta_utils.client.Duck_SDL2Controller;
import de.olivermakesco.bta_utils.client.SDLComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import uk.co.electronstudio.sdl2gdx.SDL2Controller;

@Environment(EnvType.CLIENT)
@Mixin(value = SDL2Controller.class, remap = false)
public class Mixin_SDL2Controller implements Duck_SDL2Controller {

    @Override
    public SDLComponent bta_utils$axis(int idx) {
        return new SDLComponent((SDL2Controller) (Object) this, idx, SDLComponent.Type.AXIS);
    }

    @Override
    public SDLComponent bta_utils$button(int idx) {
        return new SDLComponent((SDL2Controller) (Object) this, idx, SDLComponent.Type.BUTTON);
    }
}
