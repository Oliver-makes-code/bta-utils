package de.olivermakesco.bta_utils.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.java.games.input.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(targets = {"net.java.games.input.DIIdentifierMap"}, remap = false)
public class Mixin_DIIdentifierMap {
    @Inject(
            method = "getButtonIdentifier",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void returnUnknownIfNull(int id, CallbackInfoReturnable<Component.Identifier.Button> cir) {
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(Component.Identifier.Button.UNKNOWN);
        }
    }
}
