package com.githubclient.exception;

import org.springframework.web.client.RestClientException;

public class GitHubClientException extends Throwable {

    public GitHubClientException(RestClientException restClientException) {
    }
    public GitHubClientException(final String message) {
        super(message);
    }

    public GitHubClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
