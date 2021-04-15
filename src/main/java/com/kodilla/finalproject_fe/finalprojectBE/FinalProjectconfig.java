package com.kodilla.finalproject_fe.finalprojectBE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FinalProjectconfig {
    @Value("${finalProject.api.endpoint}")
    private String finalProjectApiEndpoint;

    public String getFinalProjectApiEndpoint() {
        return finalProjectApiEndpoint;
    }
}
