package com.premonition.enums;

public enum KazubotCommandType {

    EVENT("event");

    private final String commandType;

    KazubotCommandType(final String commandType) {
        this.commandType = commandType;
    }

    String getCommandTypeAsString() {
        return this.commandType;
    }
}
