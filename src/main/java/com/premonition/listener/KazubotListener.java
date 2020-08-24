package com.premonition.listener;

import com.premonition.enums.KazubotCommandType;
import com.premonition.model.FreeCompanyEvent;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Class to implement the behavior of Kazubot.
 * <p>
 * See https://discord.com/developers/applications/ if the bot permissions needs to be adjusted.
 */
public class KazubotListener extends ListenerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(KazubotListener.class);

    private final Map<User, FreeCompanyEvent> pendingEvents;

    public KazubotListener(final Map<User, FreeCompanyEvent> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final User author = event.getAuthor();
        final String name = author.getName();
        final Message message = event.getMessage();
        final String content = message.getContentDisplay();

        if (author.isBot()) {
            LOG.info("Received message from bot {}, ignoring", name);
            return;
        }

        LOG.debug("Received message from channel {} and user {}: {}",
                event.getChannel().getName(),
                author.getName() + "#" + author.getDiscriminator(),
                content);

        boolean botCommandMarker = "!".equals(content.substring(0, 1).stripLeading());

        if (!botCommandMarker) {
            return;
        }

        final String com = content.substring(1).toLowerCase().stripTrailing();
        LOG.info("Command string {}", com);

        final KazubotCommandType command = EnumUtils.getEnumIgnoreCase(KazubotCommandType.class, com);

        if (command == null) {
            LOG.warn("Invalid command type {}, ignoring", com);
            return;
        }

        LOG.warn("Received request for {}", command);

        if (command == KazubotCommandType.EVENT) {
            RestAction<PrivateChannel> privateChannel = author.openPrivateChannel();

            privateChannel.queue((channel) ->
                    channel.sendMessage("Hi! I'll be guiding you through setting up the event. You can cancel " +
                            "this process any time by sending the word cancel.").queue());
            privateChannel.queue((channel) ->
                    channel.sendMessage("Please provide a name for your event.").queue());

            FreeCompanyEvent fcEvent = new FreeCompanyEvent();
            fcEvent.setEventCreationTime(LocalDateTime.now());

            this.pendingEvents.put(author, fcEvent);

        }
    }
}
