package de.olivermakesco.bta_utils.mixin.server;

import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, remap = false)
public class Mixin_MinecraftServer {
    @Inject(
            method = "initiateShutdown",
            at = @At("RETURN")
    )
    void sendStopMessage(CallbackInfo ci) {
        DiscordChatRelay.sendMessageAsBot("**Server stopped.**");
    }
}
