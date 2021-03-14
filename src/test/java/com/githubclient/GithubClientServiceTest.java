package com.githubclient;

import com.githubclient.exception.GithubClientExection;
import com.githubclient.service.GithubClientService;
import com.githubclient.vo.GitHubUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * TODO
 * Añadir mock test https://www.baeldung.com/spring-mock-rest-template
 *
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@RestClientTest(GithubClientService.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class GithubClientServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubClientServiceTest.class);

    @Autowired
    private final GithubClientService githubClientService=null;

    @Test
    public void GitHubService_Should_Be_Not_Null (){
        Assert.assertNotNull(githubClientService);
    }

    @Test
    public void Get_All_Barcelona_Users_Should_Return_OK() throws GithubClientExection {

        List<GitHubUser> users = githubClientService.getAllBarcelonaUsers();

        Assert.assertNotNull(users);

    }

    @Test
    public void GetContributors_Paging_With_One_Page_And_Limit_One_Should_Return_The_Best_Contributor() throws GithubClientExection {
        String users = githubClientService.getContributorsByCity("barcelona",1,1);
        Assert.assertNotNull(users);
        //Assert.assertEquals(1, users.size());
    }
}