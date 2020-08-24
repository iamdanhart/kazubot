package com.premonition.enums;

public enum FreeCompanyRole {

    ANTEVORTA(true),

    KAZUKEI(true),

    SEEKER(true),

    MEMBER(false);

    private final boolean canMakeEvents;

    FreeCompanyRole(boolean canMakeEvents) {
        this.canMakeEvents = canMakeEvents;
    }

    boolean getCanMakeEvents() {
        return this.canMakeEvents;
    }
}
