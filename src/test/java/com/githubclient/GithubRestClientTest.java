package com.githubclient;

import com.githubclient.service.GithubClientService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@RestClientTest(GithubClientService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class GithubRestClientTest {

    @Autowired
    private RestTemplateBuilder builder;

   @Test
    public void RestTemplateBuilder_Is_No_Null() {
       Assert.assertNotNull(builder);

    }
}
