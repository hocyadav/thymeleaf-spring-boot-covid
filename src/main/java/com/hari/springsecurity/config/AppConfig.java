package com.hari.springsecurity.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {
    String simpleKey;
    String allCountriesURL;
    String singleCountryURL;
    String singleCountryHistoryURL;
    String allCountryHistoryURL;
}
