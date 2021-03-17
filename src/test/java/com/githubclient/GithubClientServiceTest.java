package com.githubclient;

import com.githubclient.service.GithubClientService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO
 * Añadir mock test https://www.baeldung.com/spring-mock-rest-template
 *                  https://www.baeldung.com/spring-rest-template-error-handling
 *
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@RestClientTest(GithubClientService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class GithubClientServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubClientServiceTest.class);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Autowired
    private final GithubClientService githubClientService=null;

    @Test
    public void GitHubClientService_Should_Be_Not_Null (){
        Assert.assertNotNull(githubClientService);
    }
}