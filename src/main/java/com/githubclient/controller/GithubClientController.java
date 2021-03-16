package com.githubclient.controller;

import com.githubclient.exception.GithubClientExection;
import com.githubclient.service.GithubClientService;
import com.githubclient.vo.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class GithubClientController {

    private final GithubClientService githubClientService;


    @Autowired
    public  GithubClientController (GithubClientService ghService){
        this.githubClientService = ghService;
    }

    @GetMapping(value = "ranking")
    @ResponseBody
    public String getContributorsByCity(@RequestParam(required = false,value = "location", defaultValue = "barcelona") String location,
                                        @RequestParam(required = false,value = "page", defaultValue = "1") int page,
                                        @RequestParam(required = false,value = "limit", defaultValue = "10") int limit) throws GithubClientExection {

        return githubClientService.getContributorsByCity(location, page, limit);
    }

    @GetMapping(value = "bcn_ranking")
    @ResponseBody
    public List<GitHubUser> getBarcelonaUsers() throws GithubClientExection {

        return githubClientService.getAllBarcelonaUsers();
    }

    @GetMapping(value = "rate_limit")
    @ResponseBody
    public String getRateLimit() throws GithubClientExection {

        return githubClientService.getRateLimit();
    }

}
