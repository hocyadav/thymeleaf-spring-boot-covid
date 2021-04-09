package com.hari.springsecurity.entity;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hariom Yadav
 * @create 09-04-2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TimeLineLocalDate {
    Map<LocalDate, BigInteger> cases = new HashMap<>();
    Map<LocalDate, BigInteger> deaths = new HashMap<>();
    Map<LocalDate, BigInteger> recovered = new HashMap<>();
}
