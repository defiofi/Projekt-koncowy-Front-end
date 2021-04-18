package com.kodilla.finalproject_fe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateOfExchange {
    @JsonProperty("currency")
    private String currencyName;
    @JsonProperty("code")
    private String currencyCode;
    @JsonProperty("bid")
    private Double bid;
    @JsonProperty("ask")
    private Double ask;

    public RateOfExchange(String currencyName, String currencyCode, Double bid, Double ask ) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.bid = bid;
        this.ask = ask;

    }
    public RateOfExchange(){
        this.currencyCode = "";
        this.bid = 0.0;
        this.ask = 0.0;
    }

    public String getCurrencyName() { return currencyName; }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Double getBid() {
        return bid;
    }

    public Double getAsk() {
        return ask;
    }

}
