package com.trip.constant;

public enum Category { // 국내지역
    // 국내
    INCHEON("인천"),
    BUSAN("부산"),
    YANGYANG("양양"),
    JEJU("제주"),
    SEOUL("서울"),
    DAEJEON("대전"),

    // 해외
    HAWAII("하와이"),
    USA("미국"),
    PHILIPPINES("필리핀"),
    NHA_TRANG("나트랑"),
    KOTA_KINABALU("코타키나발루"),
    JAPAN("일본");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
