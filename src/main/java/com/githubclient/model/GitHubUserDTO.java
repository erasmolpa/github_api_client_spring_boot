package com.githubclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserDTO {

    private String login;

    private Integer id;

    private String nodeId;

    private String avatarUrl;

    private String gravatarId;

    private String url;

    private String htmlUrl;

    private String followersUrl;

    private String followingUrl;

    private String gistsUrl;

    private String starredUrl;

    private String subscriptionsUrl;

    private String organizationsUrl;

    private String reposUrl;

    private String eventsUrl;

    private String receivedEventsUrl;

    private String type;

    private Boolean siteAdmin;
}