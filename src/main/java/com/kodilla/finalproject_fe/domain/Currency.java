package com.kodilla.finalproject_fe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
    @JsonProperty("currencyName")
    private String currencyName;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("account")
    private BigDecimal account;

    public Currency(){}

    public Currency(String currencyName, String currencyCode, BigDecimal account) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.account = account;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency that = (Currency) o;

        if (currencyName != null ? !currencyName.equals(that.currencyName) : that.currencyName != null) return false;
        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null) return false;
        return account != null ? account.equals(that.account) : that.account == null;
    }

    @Override
    public int hashCode() {
        int result = currencyName != null ? currencyName.hashCode() : 0;
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}


