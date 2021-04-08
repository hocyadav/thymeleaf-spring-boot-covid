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
public class WorldDataTableEntity {
    String name;

    Integer totalCase;
    Integer newCase;

    Integer totalDeath;
    Integer newDeath;
}
