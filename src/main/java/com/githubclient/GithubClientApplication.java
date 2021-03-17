package com.githubclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GithubClientApplication {

    //@TODO , Configure for autoredirect to the api path
    public static void main(String[] args) {
        SpringApplication.run(GithubClientApplication.class, args);
    }

}
