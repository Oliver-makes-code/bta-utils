package de.olivermakesco.bta_utils.server;

import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import de.olivermakesco.bta_utils.BtaUtilsMod;
import de.olivermakesco.bta_utils.config.BtaUtilsConfig;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.server.MinecraftServer;

public class DiscordChatRelay {
    public static void sendToMinecraft(String author, String message) {
        MinecraftServer server = MinecraftServer.getInstance();
        message = "[" + TextFormatting.PURPLE + "DISCORD" + TextFormatting.RESET + "] <" + author + "> " + message;
        BtaUtilsMod.info(message);
        String[] lines = message.split("\n");
        for (String chatMessage : lines) {
            server.configManager.sendEncryptedChatToAllPlayers(
                    chatMessage
            );
        }
    }

    public static void sendToDiscord(String author, String message) {
        if (DiscordClient.jda == null) {
            return;
        }

        JDAWebhookClient webhook = DiscordClient.getWebhook();
        if (webhook == null) {
            return;
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(author);
        builder.setAvatarUrl("https://visage.surgeplay.com/face/256/"+author);
        builder.setContent(message);
        webhook.send(builder.build());
    }

    public static void sendJoinLeaveMessage(String username, boolean joined) {
        sendMessageAsBot("**"+username+" "+(joined ? "joined" : "left")+" the game.**");
    }

    public static void sendMessageAsBot(String message) {
        JDAWebhookClient webhook = DiscordClient.getWebhook();

        if (webhook == null) {
            return;
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername("Server");
        builder.setAvatarUrl("https://static.miraheze.org/btawiki/thumb/b/bf/Bta-basket.png/160px-Bta-basket.png");
        builder.setContent(message);
        webhook.send(builder.build());
    }
}
