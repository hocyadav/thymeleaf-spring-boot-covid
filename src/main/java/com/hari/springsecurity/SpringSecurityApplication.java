package com.hari.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hari.springsecurity.config.AppConfig;
import com.hari.springsecurity.entity.EntityHistoryData;
import com.hari.springsecurity.entity.SpecificCountry;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
        final EntityHistoryData historyData = countryDataService.getSingleCountryHistoryData();

        stringToDateConversion();


        //working - use this as we can use caching properly
//        String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
//        for (String c : countries) {
//            final SpecificCountry data = singleCountryDataService.getSingleCountryData(c);
//            System.out.println("data = " + data);
//        }

        System.out.println("getSimpleKey = " + appConfig.getSimpleKey());
        System.out.println("getAllCountriesURL = " + appConfig.getAllCountriesURL());
        System.out.println("getSingleCountryURL = " + appConfig.getSingleCountryURL());
        getExample();

        Thread.sleep(3000);
        postExample_SimpleData();

        Thread.sleep(3000);
        postExample_JsonData();
    }

    private void stringToDateConversion() {
        final String[] split = "3/10/21".split("/");
        final Integer month = Integer.valueOf(split[0]);
        final Integer date = Integer.valueOf(split[1]);
        final Integer year = Integer.valueOf("20"+split[2]);
        final LocalDate localDate = LocalDate.of(year, month, date);
        System.out.println("localDate = " + localDate);
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
