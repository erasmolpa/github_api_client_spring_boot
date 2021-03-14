package com.githubclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GithubUsers {

    @JsonProperty
    private  List<GitHubUser> gitHubUsers;

   public GithubUsers(){
       gitHubUsers = new ArrayList<>();
   }
}
