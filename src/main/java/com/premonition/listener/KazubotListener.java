package com.premonition.listener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to implement the behavior of Kazubot.
 * <p>
 * See https://discord.com/developers/applications/ if the bot permissions needs to be adjusted.
 */
public class KazubotListener extends ListenerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(KazubotListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOG.info("Received message from {}: {})",
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay());

        User author = event.getAuthor();
        Message message = event.getMessage();

        LOG.info("User info");
        LOG.info(author.toString());
        System.out.println("User is");
        System.out.println(author.toString());


        if (event.isFromType(ChannelType.PRIVATE)) {
            LOG.info("Received private message from {}:{}",
                    author.getName() + "#" + author.getDiscriminator(),
                    message.getContentDisplay());
        } else {
            LOG.info("Received message from channel {} and user {}: {}",
                    event.getTextChannel().getName(),
                    author.getName() + "#" + author.getDiscriminator(),
                    message.getContentDisplay());
        }


    }
}
