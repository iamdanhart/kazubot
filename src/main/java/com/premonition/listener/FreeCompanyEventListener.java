package com.premonition.listener;

import com.premonition.model.FreeCompanyEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FreeCompanyEventListener extends ListenerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(FreeCompanyEventListener.class);

    private final Map<User, FreeCompanyEvent> pendingEvents;

    public FreeCompanyEventListener(final Map<User, FreeCompanyEvent> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        String messageContent = message.getContentDisplay();

        if (event.getAuthor().isBot()) {
            return;
        }

        LOG.debug("Received private message from {}:{}",
                author.getName() + "#" + author.getDiscriminator(),
                message.getContentDisplay());

        // if the user hasn't initiated an event, obviously don't bother them
        if (!pendingEvents.containsKey(author)) {
            return;
        }

        FreeCompanyEvent fcEvent = pendingEvents.get(author);

        if ("cancel".equalsIgnoreCase(messageContent.stripLeading().stripTrailing())) {
            pendingEvents.remove(author);
            event.getChannel().sendMessage("Event creation canceled.").queue();
            return;
        }

        // order of messages expected: name, description, time
        if (fcEvent.getName() == null || StringUtils.isEmpty(fcEvent.getName())) {
            fcEvent.setName(messageContent);
            event.getChannel().sendMessage("Please provide a description for your event.").queue();

            return;
        }

        if (fcEvent.getDescription() == null || StringUtils.isEmpty(fcEvent.getName())) {
            fcEvent.setDescription(messageContent);
            event.getChannel().sendMessage(
                    "Please pick a timezone from the following list:\n" +
                            "1. Eastern Standard Time (US & Canada)\n" +
                            "2. Pacific Standard Time (US & Canada)").queue();

            return;
        }

        if (fcEvent.getTimezone() == null) {
            int timezoneId;
            try {
                timezoneId = Integer.parseInt(messageContent.stripLeading().stripTrailing());
            } catch (final NumberFormatException nfe) {
                event.getChannel().sendMessage("Sorry, I couldn't parse that choice. Can you try again?").queue();
                return;
            }
            if (timezoneId < 0 || timezoneId > 2) {
                event.getChannel().sendMessage("It's gotta be 1 or 2, buddy.").queue();
                return;
            }

            fcEvent.setTimezone(timezoneId == 1 ? TimeZone.getTimeZone("EST") : TimeZone.getTimeZone("PST"));

            event.getChannel().sendMessage("Please provide a time for your event in one of these two formats " +
                    "(month before day, I'm an American bot): 2020-08-24 7:00 pm OR 2020-08-24 19:00").queue();

            return;
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("[yyyy-MM-dd hh:mm a][yyyy-MM-dd HH:mm]")
                .toFormatter(Locale.US);
        try {
            fcEvent.setTimeOfEvent(LocalDateTime.parse(messageContent, formatter));
        } catch (final DateTimeParseException e) {
            LOG.info("Failed to parse date: {}", e.getMessage());
            event.getChannel().sendMessage(
                    "Sorry, that date could not be parsed correctly.  Please try again.").queue();
            return;
        }

        // TODO establish event and send message to events channel
        TextChannel textChannel = event.getJDA().getTextChannelsByName("bot-test",true).get(0);
        textChannel.sendMessage("Filler message - this will be where the event goes!").queue();

        // remove event from map if successfully created
        pendingEvents.remove(author);
    }
}
