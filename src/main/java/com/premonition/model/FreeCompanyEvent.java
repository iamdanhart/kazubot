package com.premonition.model;

import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

public class FreeCompanyEvent {

    List<User> accepted;
    List<User> rejected;
    List<User> tentative;
    private String name;
    private String description;
    private LocalDateTime timeOfEvent;
    private TimeZone timezone;
    private LocalDateTime eventCreationTime; // this is for bookkeeping purposes, so doesn't need timezone

    public FreeCompanyEvent() {

    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getEventCreationTime() {
        return eventCreationTime;
    }

    public void setEventCreationTime(LocalDateTime eventCreationTime) {
        this.eventCreationTime = eventCreationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(LocalDateTime timeOfEvent) {
        this.timeOfEvent = timeOfEvent;
    }

    public List<User> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<User> accepted) {
        this.accepted = accepted;
    }

    public List<User> getRejected() {
        return rejected;
    }

    public void setRejected(List<User> rejected) {
        this.rejected = rejected;
    }

    public List<User> getTentative() {
        return tentative;
    }

    public void setTentative(List<User> tentative) {
        this.tentative = tentative;
    }
}
