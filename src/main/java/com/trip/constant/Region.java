package com.trip.constant;

public enum Region {
    Incheon("인천"),
    Busan("부산"),
    Deagu("대구"),
    Chungju("충주"),
    Gwangju("광주"),
    Yangyang("양양"),
    Jeju("제주");
    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }



}
