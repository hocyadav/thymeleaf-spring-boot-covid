package com.hari.springsecurity.entity;

import lombok.*;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Popularity {
    int inDays;
    int value1;
    int value2;
}
