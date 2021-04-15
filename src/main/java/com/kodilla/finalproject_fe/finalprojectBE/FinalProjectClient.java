package com.kodilla.finalproject_fe.finalprojectBE;

import com.kodilla.finalproject_fe.domain.RateOfExchangeDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Component
public class FinalProjectClient {
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    //private final FinalProjectconfig finalProjectConfig = new FinalProjectconfig();

    public FinalProjectClient(){

    }

    private URI getURIBuild(String addHttp){
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080" + addHttp)
                .queryParam("fields", "currency", "code", "bid", "ask")
                .build()
                .encode()
                .toUri();
        return uri;
    }
    public List<RateOfExchangeDTO> getActualRates() {
        URI uri = getURIBuild("/project/exchange");
        RateOfExchangeDTO[] finalProjectResponse = restTemplate.getForObject(uri , RateOfExchangeDTO[].class);
        List<RateOfExchangeDTO> list = Optional.ofNullable(finalProjectResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
        if(list.size()>0) {
            return list;
        }else{
            return new ArrayList<>();}
    }
}
