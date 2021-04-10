package com.hari.springsecurity.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hari.springsecurity.config.AppConfig;
import com.hari.springsecurity.entity.*;
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
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.hari.springsecurity.service.UtilService.getMapper;

/**
 * @Author Hariom Yadav
 * @create 08-04-2021
 */
@Service
public class CountryDataService {
    @Autowired
    AppConfig appConfig;

    public Map<String, Object> getUIWorldMap() {
        final List<EntityHistoryData_LocalDate> data = getAllCountryHistoryLocalDate();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Country", "Cases");
        data.forEach(d -> {
            final String country = d.getCountry();
            final Map.Entry<LocalDate, BigInteger> case_ =
                    d.getTimeline().getCases().entrySet().stream().findAny().get();
            final BigInteger caseCount = case_.getValue();
            map.put(country, caseCount);
        });
        System.out.println("map = " + map);
        return map;
    }

    public List<EntityHistoryData_LocalDate> getAllCountryHistoryLocalDate() {
        final List<EntityHistoryData> allCountryHistoryData = getAllCountryHistoryData();
        return allCountryHistoryData.stream().map(c -> convertToLocalDateEntity(c)).collect(Collectors.toList());
    }

    @SneakyThrows
    @Cacheable(value = "allCountryHistoryData")
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

    public List<TimeLineEntity> groupByDate(String name) {
        final EntityHistoryData_LocalDate india = getSingleCountryHistoryData_LocalDate(name);
        final TimeLineLocalDate timeline = india.getTimeline();
        final Map<LocalDate, BigInteger> cases = timeline.getCases();
        final Map<LocalDate, BigInteger> deaths = timeline.getDeaths();
        List<TimeLineEntity> list = new ArrayList<>();

        cases.forEach((localDate, cases_) -> {
            final BigInteger death = deaths.get(localDate);
            final TimeLineEntity entity = getTimeLineEntity(localDate, cases_, death);
            list.add(entity);
        });
        return list;
    }

    private TimeLineEntity getTimeLineEntity(LocalDate localDate, BigInteger cases_, BigInteger death) {
        final TimeLineEntity entity = TimeLineEntity.builder().date(localDate)
                .entity1(TimeLineSingleEntity.builder().value(cases_).build())
                .entity2(TimeLineSingleEntity.builder().value(death).build())
                .build();
        return entity;
    }

    @Cacheable(value = "singleCountryHistoryCache", key = "#name")//working
    public EntityHistoryData_LocalDate getSingleCountryHistoryData_LocalDate(String name) {
        final EntityHistoryData historyData = getSingleCountryHistoryData(name);
        EntityHistoryData_LocalDate data_localDate = convertToLocalDateEntity(historyData);
        return data_localDate;
    }

    @SneakyThrows
    private EntityHistoryData getSingleCountryHistoryData(String name) {//working
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
                List<SpecificCountry> participantJsonList = mapper.readValue(result, new TypeReference<List<SpecificCountry>>() {
                });
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

    private EntityHistoryData_LocalDate convertToLocalDateEntity(EntityHistoryData historyData) {
        EntityHistoryData_LocalDate result = new EntityHistoryData_LocalDate();
        result.setCountry(historyData.getCountry());
        Map<LocalDate, BigInteger> casesMap = new HashMap<>();
        Map<LocalDate, BigInteger> deathMap = new HashMap<>();
        Map<LocalDate, BigInteger> recoverMap = new HashMap<>();
        historyData.getTimeline().getCases().forEach((date, count) -> casesMap.put(stringToDateConversion(date), count));
        historyData.getTimeline().getDeaths().forEach((date, count) -> deathMap.put(stringToDateConversion(date), count));
        historyData.getTimeline().getDeaths().forEach((date, count) -> recoverMap.put(stringToDateConversion(date), count));

        result.setTimeline(TimeLineLocalDate.builder().cases(casesMap).deaths(deathMap).recovered(recoverMap).build());
        return result;
    }

    private LocalDate stringToDateConversion(String s) {
        final String[] arr = s.split("/");
        final Integer month = Integer.valueOf(arr[0]);
        final Integer date = Integer.valueOf(arr[1]);
        final Integer year = Integer.valueOf("20" + arr[2]);
        final LocalDate localDate = LocalDate.of(year, month, date);
        return localDate;
    }
}
