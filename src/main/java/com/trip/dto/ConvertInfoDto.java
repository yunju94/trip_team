package com.trip.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConvertInfoDto {
    @NotBlank
    private String receiveCountry;// 수취 국가

    @Min(0)
    @Max(10000)
    private double sendAmount;// 보내는 금액
}
