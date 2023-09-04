package de.olivermakesco.bta_utils;

import de.olivermakesco.bta_utils.server.DiscordChatRelay;
import de.olivermakesco.bta_utils.server.DiscordClient;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BtaUtilsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("BTA Utils");

    @Override
    public void onInitialize() {
        new Thread(() -> {
            if (DiscordClient.init()) {
                DiscordChatRelay.sendMessageAsBot("**Server Started**");
            }
        }).start();
    }

    public static void info(String s) {
        LOGGER.info(s);
    }
}
