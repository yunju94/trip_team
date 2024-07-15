package com.trip.service;

import java.util.List;

public interface CurrencyConverterService {
    Double getCurrencyRate(String receiveCountry);
    List<Double> getPHPDB();
    List<Double> getJPYDB();
    List<Double> getUSDDB();
    List<Double> getVNDDB();
    List<Double> getMYRDB();

}
