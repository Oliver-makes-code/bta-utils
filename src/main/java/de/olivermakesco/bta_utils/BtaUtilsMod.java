package de.olivermakesco.bta_utils;

import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import de.olivermakesco.bta_utils.server.DiscordClient;
import net.fabricmc.api.ModInitializer;

public class BtaUtilsMod implements ModInitializer {
    @Override
    public void onInitialize() {
        new Thread(() -> {
            DiscordClient.init();
            DiscordChatRelay.sendMessageAsBot("**Server Started**");
        }).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DiscordChatRelay.sendMessageAsBot("**Server Stopped**");
        }));
    }
}
