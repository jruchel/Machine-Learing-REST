package com.jruchel.mlrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MlRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MlRestApplication.class, args);
    }

}
//TODO associate user projects with the data not with the particular models, allow for multiple algorithms to be used in multiple different ways on the same data
