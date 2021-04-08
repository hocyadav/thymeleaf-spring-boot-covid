package com.hari.springsecurity.service;

import com.hari.springsecurity.entity.WorldDataTableEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Service
public class WorldDataService {

    public List<WorldDataTableEntity> getAllCountryData() {
        List<WorldDataTableEntity> list = new ArrayList<>();
        //todo: call gateway service get data

        return list;
    }



}
