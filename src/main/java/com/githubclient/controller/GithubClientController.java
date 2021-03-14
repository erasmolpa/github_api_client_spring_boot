package com.githubclient.controller;

import com.githubclient.exception.GithubClientExection;
import com.githubclient.service.GithubClientService;
import com.githubclient.vo.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class GithubClientController {

    private final GithubClientService githubClientService;

    @Autowired
    public  GithubClientController (GithubClientService ghService){
        this.githubClientService = ghService;
    }

    @GetMapping(value = "contributors-by-city")
    public String getContributorsByCity(@RequestParam(value = "location", defaultValue = "barcelona") String location,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit) throws GithubClientExection {

        return githubClientService.getContributorsByCity(location, page, limit);
    }

    @GetMapping(value = "barcelona-users")
    public List<GitHubUser> getBarcelonaUsers() throws GithubClientExection {

        return githubClientService.getAllBarcelonaUsers();
    }
}
