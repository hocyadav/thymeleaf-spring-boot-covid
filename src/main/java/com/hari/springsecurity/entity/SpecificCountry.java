package com.hari.springsecurity.entity;

import lombok.*;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SpecificCountry {
    public long updated;
    public String country;
    public CountryInfo countryInfo;
    public int cases;
    public int todayCases;
    public int deaths;
    public int todayDeaths;
    public int recovered;
    public int todayRecovered;
    public int active;
    public int critical;
    public int casesPerOneMillion;
    public int deathsPerOneMillion;
    public int tests;
    public int testsPerOneMillion;
    public int population;
    public String continent;
    public int oneCasePerPeople;
    public int oneDeathPerPeople;
    public int oneTestPerPeople;
    public double activePerOneMillion;
    public double recoveredPerOneMillion;
    public double criticalPerOneMillion;
}

