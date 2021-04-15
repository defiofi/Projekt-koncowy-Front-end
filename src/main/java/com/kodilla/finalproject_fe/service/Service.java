package com.kodilla.finalproject_fe.service;

import com.kodilla.finalproject_fe.domain.RateOfExchangeDTO;
import com.kodilla.finalproject_fe.domain.UserDTO;
import com.kodilla.finalproject_fe.finalprojectBE.FinalProjectClient;
import com.kodilla.finalproject_fe.finalprojectBE.FinalProjectconfig;
import elemental.json.*;
import elemental.json.impl.JreJsonObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

public class Service {

    private RestTemplate restTemplate = new RestTemplateBuilder().build();
    private FinalProjectconfig finalProjectconfig;
    private FinalProjectClient finalProjectClient;
    private List<RateOfExchangeDTO> rates = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();
    private static Service service;

    private Service(){
    }
    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    public List<RateOfExchangeDTO> getRates() {

               URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/project/exchange")
                .queryParam("fields", "currency", "code", "bid", "ask")
                .build()
                .encode()
                .toUri();
        RateOfExchangeDTO[] finalProjectResponse = restTemplate.getForObject(uri , RateOfExchangeDTO[].class);
        rates = Optional.ofNullable(finalProjectResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());

        return rates;
    }
    public List<UserDTO> getUsers() {
        URI uriUsers = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/project/user/*")
                .queryParam("fields", "userID", "name")
                .build()
                .encode()
                .toUri();
        UserDTO[] userResponse = restTemplate.getForObject(uriUsers , UserDTO[].class);
        users = Optional.ofNullable(userResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
        return users;
    }
    public void deleteUser(Long userID){
        URI uriDeleteUsers = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/project/user/"+userID)
                .queryParam("fields", "userID")
                .build()
                .encode()
                .toUri();
        restTemplate.delete(uriDeleteUsers);
    }
    public UserDTO createUser(UserDTO userDTO) {

        URI uriCreateUsers = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/project/user/")
                .queryParam("fields", "userID")
                .build()
                .encode()
                .toUri();
        UserDTO userResponse = restTemplate.postForObject(uriCreateUsers, userDTO, UserDTO.class);
    return userResponse;
    }
    public void  changeUserName(UserDTO userDTO){
        URI uriChangeUsers = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/project/user/")
                .queryParam("userId", userDTO.getUserID())
                .queryParam("userName", userDTO.getUserName())
                .build()
                .encode()
                .toUri();
         restTemplate.put(uriChangeUsers, userDTO);
    }
}
