package com.hari.springsecurity.entity;

import lombok.*;

import java.math.BigInteger;
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
@ToString
public class TimeLine {
    Map<String, BigInteger> cases = new HashMap<>();
    Map<String, BigInteger> deaths = new HashMap<>();
    Map<String, BigInteger> recovered = new HashMap<>();
}
