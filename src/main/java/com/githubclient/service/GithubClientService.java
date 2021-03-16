package com.githubclient.service;

import com.githubclient.exception.GithubClientExection;
import com.githubclient.vo.GitHubUser;
import com.githubclient.vo.GithubUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
@PropertySource(value = {"classpath:application.yaml"})
public class GithubClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubClientService.class);
    private final RestTemplate restTemplate;

    @Autowired
    public GithubClientService(RestTemplate restTemplate){
     this.restTemplate = restTemplate;
    }

    @Value("${GITHUB_API.URL}")
    private String API_URL;
    private final String SEARCH_USERS_PATH = "search/users?";

    public List<GitHubUser> getAllBarcelonaUsers() throws GithubClientExection {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(API_URL+ SEARCH_USERS_PATH + "l=&o=desc")
                .queryParam("q","location:barcelona")
                .queryParam("s","repositories")
                .queryParam("type","Users");
        try {
            return restTemplate.getForObject(uriBuilder.toUriString(), GithubUsers.class).getGitHubUsers();
        }catch (RestClientException e){
            throw new GithubClientExection(e);
        }

    }

    public String getContributorsByCity(String city, int page, int limit) throws GithubClientExection {
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
            //return restTemplate.getForObject(uriBuilder.toUriString(), GithubUsers.class).getGitHubUsers();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            //@TODO Set user Agent ?
            HttpEntity<GitHubUser> requestEntity = new HttpEntity<>(null, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            if(response.hasBody()){
                gitHubUsers = response.getBody();
            }

        }catch (RestClientException e){
            throw new GithubClientExection(e);
        }

        LOGGER.info(gitHubUsers.toString());
       return gitHubUsers;
    }

    public String getRateLimit() throws GithubClientExection {
        String rateLimit = "";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(API_URL+ "/rate_limit");
        try {
            //return restTemplate.getForObject(uriBuilder.toUriString(), GithubUsers.class).getGitHubUsers();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            //@TODO Set user Agent ?
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    String.class);
            if(response.hasBody()){
                rateLimit= response.getBody().toString();
            }
        }catch (RestClientException e){
            throw new GithubClientExection(e);
        }
        return rateLimit;
    }
}
