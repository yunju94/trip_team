package com.trip.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyDto {
    private boolean success;//api성공 여부 	쿼리가 성공하는지 여부에 따라 true또는 를 반환합니다 .false
    private int timestamp;//api응답시간 	환율이 수집된 정확한 날짜와 시간(UNIX)을 반환합니다.
    private String source;//원본 통화 문자열 모든 환율이 상대적인 통화를 반환합니다. (기본값: USD)
    private Map<String, Double> quotes; //국가별 환율 정보 통화 쌍과 해당 환율을 포함한 모든 환율 값이 포함되어 있습니다
    private Map<String, String> error;//api 실패 시 오류 정보
}
