package com.hari.springsecurity.entity;

import lombok.*;

import java.math.BigInteger;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TimeLineSingleEntity {
    BigInteger value;
    String title;
    String text;
}
