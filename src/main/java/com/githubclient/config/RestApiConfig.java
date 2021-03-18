package com.githubclient.config;

import com.githubclient.exception.HttpClientErrorHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestApiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiConfig.class);

    /**
     * Using {@link CloseableHttpClient} to establish and optimize the http client connection against the external api
     * @see HttpClientConfig
     */

    final CloseableHttpClient httpClient;

    public RestApiConfig(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * @TODO FIXME Set custom loggin interceptor. to improve the visibility in the traces.
     * //restTemplate.setInterceptors(Collections.singletonList(new CustomLoggingInterceptor()));
     */
    @Bean
    public RestTemplate restTemplate() {
        LOGGER.info("initializing Rest template bean");
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
                     restTemplate.setErrorHandler(new HttpClientErrorHandler());
        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        LOGGER.info("Setting Apache httpClient as http Factory Connection Provider...");
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(100);
        return scheduler;
    }
}

