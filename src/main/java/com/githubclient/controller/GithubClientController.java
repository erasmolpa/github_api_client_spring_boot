package com.githubclient.controller;

import com.githubclient.exception.GitHubClientException;
import com.githubclient.service.GithubClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GithubClientController {

    private final GithubClientService githubClientService;


    @Autowired
    public GithubClientController(@Qualifier("githubService") GithubClientService ghService) {
        this.githubClientService = ghService;
    }

    @GetMapping(value = "ranking")
    @ResponseBody
    public String getRankingByCity(@RequestParam(required = false, value = "location", defaultValue = "barcelona") String location,
                                        @RequestParam(required = false, value = "page", defaultValue = "1") int page,
                                        @RequestParam(required = false, value = "limit", defaultValue = "10") int limit) throws GitHubClientException {

        return githubClientService.getRankingByCity(location, page, limit);
    }

    @GetMapping(value = "rate_limit")
    @ResponseBody
    public String getRateLimit() throws GitHubClientException {

        return githubClientService.getRateLimit();
    }

}

