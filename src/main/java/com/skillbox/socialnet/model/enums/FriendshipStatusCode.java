package com.skillbox.socialnet.model.enums;

public enum FriendshipStatusCode {
    REQUEST("Request"),
    FRIEND("Friend"),
    BLOCKED("Blocked"),
    DECLINED("Declined"),
    SUBSCRIBED("Subscribed");

    private final String name;

    FriendshipStatusCode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
