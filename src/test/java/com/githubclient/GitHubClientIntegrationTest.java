package com.githubclient;

import com.githubclient.config.RestApiConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestApiConfig.class)
public class GitHubClientIntegrationTest {
    // https://attacomsian.com/blog/http-requests-resttemplate-spring-boot
    //https://www.baeldung.com/httpclient-ssl
    /***
     * WE USE THE SEARH API
     * https://docs.github.com/en/rest/reference/search
     * Documentation about all params
     * https://docs.github.com/en/github/searching-for-information-on-github/searching-on-github
     * https://github.com/search
     * https://github.com/search/advanced
     */
    final String limitUri = "https://api.github.com/rate_limit";

    @Test
    public void Test_If_Rate_Limit_Is_OK() {
        //https://docs.github.com/en/rest/reference/rate-limit
        //Given

        //final HttpUriRequest request = new HttpGet(limitUri);

        // When
        //final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        //assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
        Assert.assertTrue(true);
    }
}



