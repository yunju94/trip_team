package com.trip.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    String apiKey = "0241557301001773";
    String secretKey = "DN9CDxdGNMIXyaftNXgeBdLB071AfkdmVzOoSpJ4adxfR9Ru4M212IDJCyUYkFzIRICW5WiX6LMyHGQ3";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}
