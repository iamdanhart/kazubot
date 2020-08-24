package com.premonition.enums;

public enum FreeCompanyEventActions {

    CREATE("create"),

    ALTER("alter");

    private final String action;

    FreeCompanyEventActions(final String action) {
        this.action = action;
    }

    String getActionAsString() {
        return action;
    }
}


