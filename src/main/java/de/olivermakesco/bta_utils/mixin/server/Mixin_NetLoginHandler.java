package de.olivermakesco.bta_utils.mixin.server;

import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = NetLoginHandler.class, remap = false)
public class Mixin_NetLoginHandler {
    @Inject(
            method = "doLogin",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/core/net/packet/Packet;)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    void sendLoginMessage(Packet1Login packet1login, CallbackInfo ci, EntityPlayerMP player) {
        String username = player.getDisplayName().replaceFirst("^§0", "");
        DiscordChatRelay.sendJoinLeaveMessage(username, true);
    }
}
