package guru.springframework.springrestclientexamples.controllers;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    public void index() throws Exception {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void formPost() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("limit", "3");

        webTestClient.post().uri("users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(map))
                .exchange()
                .expectStatus().isOk();
    }
}