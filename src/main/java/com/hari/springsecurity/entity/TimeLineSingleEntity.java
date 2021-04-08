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
public class TimeLineSingleEntity {
    Integer value;
    String title;
    String text;
}
