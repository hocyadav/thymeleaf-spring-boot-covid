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
public class CountryInfo {
    public int _id;
    public String iso2;
    public String iso3;
    public double lat;
    public String flag;
}
