package com.hari.springsecurity.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hari.springsecurity.config.AppConfig;
import com.hari.springsecurity.entity.EntityHistoryData;
import com.hari.springsecurity.entity.SpecificCountry;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.hari.springsecurity.service.UtilService.getMapper;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Service
public class CountryDataService {
    @Autowired
    AppConfig appConfig;

    @SneakyThrows
    public List<EntityHistoryData> getAllCountryHistoryData() {
        final String url = appConfig.getAllCountryHistoryURL();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                final ObjectMapper mapper = getMapper();
                final List<EntityHistoryData> historyData = mapper.readValue(result, new TypeReference<List<EntityHistoryData>>() {
                });
                return historyData;
            }
        }
        return new LinkedList<>();
    }


    @SneakyThrows
    public EntityHistoryData getSingleCountryHistoryData(String name) {//working
        final String historyURL = appConfig.getSingleCountryHistoryURL().replace("country_name", name);
        HttpGet request = new HttpGet(historyURL);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("-----------");
                System.out.println(result);
                final ObjectMapper mapper = getMapper();
                final EntityHistoryData historyData = mapper.readValue(result, EntityHistoryData.class);
                System.out.println("historyData = " + historyData);
                return historyData;
            }
        }

        return new EntityHistoryData();
    }

    @Cacheable(value = "singleCountryCache", key = "#countryName")//working
    public SpecificCountry getSingleCountryData(String countryName) throws IOException {//working
        System.out.println("api call");
        return apiCall(countryName);
    }

    public void getAllCountriesData() throws IOException {//working
        final String countriesURL = appConfig.getAllCountriesURL();
        HttpGet request = new HttpGet(countriesURL);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("-----all countries------");
                System.out.println(result);
                final ObjectMapper mapper = getMapper();
                List<SpecificCountry> participantJsonList = mapper.readValue(result, new TypeReference<List<SpecificCountry>>(){});
                System.out.println("participantJsonList = " + participantJsonList);
            }
        }
    }

    private SpecificCountry apiCall(String countryName) throws IOException {//working
        final String indiaUrl = appConfig.getSingleCountryURL().replace("country_name", countryName);

        HttpGet request = new HttpGet(indiaUrl);
        // add request headers
//        request.addHeader("custom-key", "mkyong");
//        request.addHeader(HttpHeaders.USER_AGENT, "PostmanRuntime/7.26.10");
//        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("-----------");
                System.out.println(result);
                return getMapper().readValue(result, SpecificCountry.class);//working
            }
        }
        return new SpecificCountry();
    }
}
