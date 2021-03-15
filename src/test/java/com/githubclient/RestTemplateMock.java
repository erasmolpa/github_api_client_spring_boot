package com.githubclient;

import com.githubclient.exception.RestTemplateResponseErrorHandler;
import com.githubclient.service.GithubClientService;
import com.githubclient.vo.GithubUsers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/***
 * @see https://howtodoinjava.com/spring-boot2/testing/restclienttest-test-services/
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { GithubUsers.class })
@RestClientTest(GithubClientService.class)
public class RestTemplateMock {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RestTemplateBuilder builder;


    @Test(expected = ResponseStatusException.class)
    public void  givenRemoteApiCall_when404Error_thenThrowNotFound() {
        Assert.assertNotNull(this.builder);
        Assert.assertNotNull(this.server);
        RestTemplate restTemplate = this.builder
                    .errorHandler(new RestTemplateResponseErrorHandler())
                    .build();

        /**
         *     this.server
         *                 .expect(ExpectedCount.once(), requestTo("/bars/4242"))
         *                 .andExpect(method(HttpMethod.GET))
         *                 .andRespond(withStatus(HttpStatus.NOT_FOUND));
         *
         *         GithubUsers response = restTemplate
         *                 .getForObject("/bars/4242", GithubUsers.class);
         */

        this.server.verify();
    }

}
