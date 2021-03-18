package com.githubclient.controller;

import com.githubclient.exception.GitHubClientException;
import com.githubclient.service.GithubClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class GithubClientController {

    private final GithubClientService githubClientService;


    @Autowired
    public GithubClientController(@Qualifier("githubService") GithubClientService ghService) {
        this.githubClientService = ghService;
    }

    @GetMapping(value = "ranking")
    @ResponseBody
    public String getContributorsByCity(@RequestParam(required = false, name = "location") String location,
                                   @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                   @RequestParam(required = false, name = "limit", defaultValue = "10") int limit) throws GitHubClientException {

        return githubClientService.getContributorsByCity(location, page, limit);
    }

    @GetMapping(value = "rate_limit")
    @ResponseBody
    public String getRateLimit() throws GitHubClientException {

        return githubClientService.getRateLimit();
    }

}

