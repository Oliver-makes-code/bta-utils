package de.olivermakesco.bta_utils.mixin.server;

import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import net.minecraft.core.entity.Entity;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerMP.class, remap = false)
public abstract class Mixin_EntityPlayerMP {
    @Shadow protected abstract String deathMessage(Entity entity);

    @Inject(
            method = "onDeath",
            at = @At("RETURN")
    )
    void sendDeathMessage(Entity entity, CallbackInfo ci) {
        String message = deathMessage(entity).replaceAll("§.", "");
        DiscordChatRelay.sendMessageAsBot("**" + message + "**");
    }
}
