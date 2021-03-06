package com.githubclient.service;

import com.githubclient.exception.GitHubClientException;
import com.githubclient.model.GitHubUserDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;

@Service
@Component(value = "githubService")
@PropertySource(value = {"classpath:application.yaml"})
public class GithubClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubClientService.class);
    private final RestTemplate restTemplate;

    @Autowired
    public GithubClientService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Value("${github_api.uri}")
    private  String API_URL;


    @Value("${github_api.search_users_path}")
    private  String SEARCH_USERS_PATH;

    //@Value("${github_api.version_spec}")
    //private String ACCEPT_HEADER;

    // Maching with the application configuration
    private static final String SERVICE_NAME= "githubService";

     @RateLimiter(name = SERVICE_NAME)
     @TimeLimiter(name = SERVICE_NAME)
    public String getContributorsByCity(String city, int page, int limit) throws GitHubClientException {
        String gitHubUsers =null;
        String location = "location:"+ city;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(API_URL+ SEARCH_USERS_PATH + "l=&o=desc")
                .queryParam("q",location)
                .queryParam("s","repositories")
                .queryParam("type","Users")
                .queryParam("page",page)
                .queryParam("per_page",limit);

        try {

            HttpEntity<GitHubUserDTO> requestEntity = new HttpEntity<>(null, getCustomHttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            if(response.hasBody()){
                gitHubUsers = response.getBody();
            }

        }catch (RestClientException e){
            throw new GitHubClientException(e);
        }

        LOGGER.info(gitHubUsers.toString());
       return gitHubUsers;
    }


    public String getRateLimit() throws GitHubClientException {
        String rateLimit = "";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(API_URL+ "/rate_limit");
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(null, getCustomHttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            if(response.hasBody()){
                rateLimit= response.getBody().toString();
            }
        }catch (RestClientException e){
            throw new GitHubClientException(e);
        }
        return rateLimit;
    }

    @TimeLimiter(name = SERVICE_NAME)
    @RateLimiter(name = SERVICE_NAME)
    public String failure() { throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");  }


    /**
     * Build a custom Header based on the github documentation.
     * @return
     */
    @NotNull
    private HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //header.(headers.USER_AGENT, "API-Network");
        //headers.add("Accept",ACCEPT_HEADER);
        return headers;
    }
}
