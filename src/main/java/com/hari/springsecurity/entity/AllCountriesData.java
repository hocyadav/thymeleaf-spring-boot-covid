package com.hari.springsecurity.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AllCountriesData {
    List<SpecificCountry> list = new ArrayList<>();
}
