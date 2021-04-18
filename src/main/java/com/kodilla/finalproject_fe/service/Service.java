package com.kodilla.finalproject_fe.service;

import com.kodilla.finalproject_fe.domain.Currency;
import com.kodilla.finalproject_fe.domain.CurrencyForm;
import com.kodilla.finalproject_fe.domain.RateOfExchange;
import com.kodilla.finalproject_fe.domain.User;
import com.kodilla.finalproject_fe.finalprojectBE.FinalProjectconfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

public class Service {
    protected final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private RestTemplate restTemplate = new RestTemplateBuilder().build();
    private FinalProjectconfig finalProjectconfig = new FinalProjectconfig();
    private List<RateOfExchange> rates = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private static Service service;

    private Service(){
    }
    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    public List<RateOfExchange> getRates() {
        URI uri = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/exchange")
                .build()
                .encode()
                .toUri();
        try {
            RateOfExchange[] finalProjectResponse = restTemplate.getForObject(uri, RateOfExchange[].class);
            rates = Optional.ofNullable(finalProjectResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
            return rates;
        }catch(RestClientException e){
            LOGGER.error("Błąd połączenia z aplikacją REST - RestClientException dla metody getRates()");
            return new ArrayList<RateOfExchange>();
        }
    }
    public List<User> getUsers() {
        URI uriUsers = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/user/*")
                .build()
                .encode()
                .toUri();
        User[] userResponse = restTemplate.getForObject(uriUsers , User[].class);
        return Optional.ofNullable(userResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
    public void deleteUser(Long userID){
        URI uriDeleteUsers = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/user/"+userID)
                .build()
                .encode()
                .toUri();
        restTemplate.delete(uriDeleteUsers);
    }
    public User createUser(User user) {
        URI uriCreateUsers = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/user/")
                .build()
                .encode()
                .toUri();
        User userResponse = restTemplate.postForObject(uriCreateUsers, user, User.class);
    return userResponse;
    }
    public void  changeUserName(User user){
        URI uriChangeUsers = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/user/")
                .queryParam("userId", user.getUserID())
                .queryParam("userName", user.getUserName())
                .build()
                .encode()
                .toUri();
         restTemplate.put(uriChangeUsers, user);
    }
    public List<Currency> getCurrency(Long userID){
        URI uriShowCurrency = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/currency/"+ userID)
                .build()
                .encode()
                .toUri();
        Currency[] currencyListResponse = restTemplate.getForObject(uriShowCurrency, Currency[].class);
        return Optional.ofNullable(currencyListResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
    public Currency newCurrency(Long userID , String currencyCode){
        URI uriNewCurrency = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/currency/")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currencyCode)
                .build()
                .encode()
                .toUri();
        Currency currencyResponse = restTemplate.postForObject(uriNewCurrency,null, Currency.class);
        return currencyResponse;
    }
    public void deleteCurrency(Long userID , String currencyCode){
        URI uriDeleteCurrency = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/currency/")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currencyCode)
                .build()
                .encode()
                .toUri();
        restTemplate.delete(uriDeleteCurrency);
    }
    public void topUpAccount(Long userID , String currencyCode , Double value){
        URI uriTopUpCurrency = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/currency/topUp")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currencyCode)
                .queryParam("value", value)
                .build()
                .encode()
                .toUri();
         restTemplate.put(uriTopUpCurrency, null);
    }
    public void payOutAccount(Long userID , String currencyCode , Double value){
        URI uriPayOutCurrency = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/currency/payOut")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currencyCode)
                .queryParam("value", value)
                .build()
                .encode()
                .toUri();
        restTemplate.put(uriPayOutCurrency, null);
    }
    public void buyCurrency(Long userID, String currency_Code, Double value){
        URI uriBuy = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/exchange/buy")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currency_Code)
                .queryParam("value", value)
                .build()
                .encode()
                .toUri();
        restTemplate.put(uriBuy, null);
    }
    public void sellCurrency(Long userID, String currency_Code, Double value){
        URI uriSell = UriComponentsBuilder.fromHttpUrl(finalProjectconfig.getApiEndpoint()+"/project/exchange/sell")
                .queryParam("userId", userID)
                .queryParam("currency_Code", currency_Code)
                .queryParam("value", value)
                .build()
                .encode()
                .toUri();
        restTemplate.put(uriSell, null);
    }
}
