package com.githubclient.exception;

import org.springframework.web.client.RestClientException;

public class GithubClientExection extends Throwable {

    public GithubClientExection(RestClientException restClientException) {
    }
    public GithubClientExection(final String message) {
        super(message);
    }

    public GithubClientExection(final String message, final Throwable cause) {
        super(message, cause);
    }
}
