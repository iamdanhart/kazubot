package com.premonition.main;

import com.premonition.listener.FreeCompanyEventListener;
import com.premonition.listener.KazubotListener;
import com.premonition.model.FreeCompanyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger.info("Starting bot");

        String token = System.getProperty("discord.token");
        Validate.notEmpty(token, "Discord token cannot be null or empty");

        JDABuilder builder = JDABuilder.createDefault(token);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.listening("requests"));

        // initialize pending events map to pass to listeners
        Map<User, FreeCompanyEvent> pendingEvents = new ConcurrentHashMap<>();

        builder.addEventListeners(
                new KazubotListener(pendingEvents),
                new FreeCompanyEventListener(pendingEvents));

       JDA jda =  builder.build().awaitReady();

       logger.info("Kazubot successfully started!");
    }
}
