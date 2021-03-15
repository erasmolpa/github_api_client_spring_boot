package com.githubclient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class HttpClientErrorHandler implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().is4xxClientError();
    }
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        LOGGER.error("HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
    }
}
