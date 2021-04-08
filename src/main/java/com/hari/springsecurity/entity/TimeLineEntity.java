package com.hari.springsecurity.entity;

import lombok.*;

import java.time.LocalDate;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TimeLineEntity {
    LocalDate date;
    TimeLineSingleEntity entity1;
    TimeLineSingleEntity entity2;
}
