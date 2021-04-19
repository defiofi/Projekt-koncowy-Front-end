package com.kodilla.finalproject_fe.finalprojectBE;

import org.springframework.stereotype.Component;

@Component
public class FinalProjectconfig {

    private String finalProjectApiEndpoint;

    public String getApiEndpoint() {
        return finalProjectApiEndpoint;
    }
    public FinalProjectconfig(){
        finalProjectApiEndpoint = "http://localhost:8080";
    }
}
