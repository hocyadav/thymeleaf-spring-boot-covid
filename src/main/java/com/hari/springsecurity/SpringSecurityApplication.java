package com.hari.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hari.springsecurity.config.AppConfig;
import com.hari.springsecurity.entity.*;
import com.hari.springsecurity.service.CountryDataService;
import com.hari.springsecurity.service.UtilService;
import lombok.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableCaching
public class SpringSecurityApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Autowired
    AppConfig appConfig;

    @Autowired
    CountryDataService countryDataService;

    public static final Map<String, SpecificCountry> specificCountryMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        final SpecificCountry india = countryDataService.getSingleCountryData("india");
        specificCountryMap.put("india", india);
        System.out.println("india = " + india);
        final SpecificCountry italy = countryDataService.getSingleCountryData("italy");
        specificCountryMap.put("italy", italy);
        System.out.println("italy = " + italy);
        final SpecificCountry india1 = countryDataService.getSingleCountryData("india");
        System.out.println("singleCountryDataService = " + india1);
        specificCountryMap.put("india", india1);
        System.out.println("specificCountryMap = " + specificCountryMap);

        countryDataService.getAllCountriesData();
        final EntityHistoryData_LocalDate india2 = countryDataService.getSingleCountryHistoryData_LocalDate("india");
        System.out.println("single country india = " + india2);

        final List<EntityHistoryData_LocalDate> countryHistoryData = countryDataService.getAllCountryHistoryLocalDate();
        System.out.println("all country = " + countryHistoryData);

        final List<TimeLineEntity> list = countryDataService.groupByDate("india");
        System.out.println("list = " + list);


        countryDataService.getUIWorldMap();

//        getExample();
//        Thread.sleep(3000);
//        postExample_SimpleData();
//        Thread.sleep(3000);
//        postExample_JsonData();
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

    private void postExample_JsonData() {
        try {
            String result = sendPOST_Json("https://httpbin.org/post");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendPOST_Json(String url) throws IOException {
        String result = "";
        HttpPost post = new HttpPost(url);

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"mkyong\",");
        json.append("\"notes\":\"hello\"");
        json.append("}");
        // send a JSON data
        post.setEntity(new StringEntity(json.toString()));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }
        return result;
    }

    private void postExample_SimpleData() {
        try {
            String result = sendPOSTRequest("https://httpbin.org/post");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendPOSTRequest(String url) throws IOException {
        String result = "";
        HttpPost post = new HttpPost(url);
        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            result = EntityUtils.toString(response.getEntity());
        }
        return result;
    }

    private void getExample() throws IOException {
        HttpGet request = new HttpGet("https://httpbin.org/get");
        // add request headers
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            // Get HttpResponse Status
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println("-----------");
                System.out.println(result);
                storeInObjectMapper(result);
            }
        }
    }

    @SneakyThrows
    private Dummy storeInObjectMapper(String result) {
        System.err.println("SpringSecurityApplication.storeInObjectMapper");
        final ObjectMapper objectMapper = UtilService.getMapper();
        final Dummy dummy = objectMapper.readValue(result, Dummy.class);
        System.out.println("dummy = " + dummy);
        return dummy;
    }
}

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Dummy {
    String origin;
    String url;
}
