package com.kodilla.finalproject_fe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    @JsonProperty("userID")
    private Long userID;
    @JsonProperty("name")
    private String userName;

    public UserDTO(){
        this.userName = "";
    }

    public UserDTO(String userName){
        this.userName = userName;
    }

    public UserDTO(Long userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (userID != null ? !userID.equals(userDTO.userID) : userDTO.userID != null) return false;
        return userName != null ? userName.equals(userDTO.userName) : userDTO.userName == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
