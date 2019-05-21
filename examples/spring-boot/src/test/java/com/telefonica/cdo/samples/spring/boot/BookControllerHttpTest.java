package com.telefonica.cdo.samples.spring.boot;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerHttpTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void booksShouldContainReusable() throws Exception {
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/books", String.class)).contains("Reusable");
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/books", String.class)).contains("Messaging");
    }

}
