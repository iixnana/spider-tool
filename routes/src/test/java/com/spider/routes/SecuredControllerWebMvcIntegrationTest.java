package com.spider.routes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RoutesApplication.class)
@AutoConfigureMockMvc
@SpringBootTest
public class SecuredControllerWebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(value = "spring")
    @Test
    public void givenAuthRequestOnPublicEndpoint_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/health").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser(value = "spring")
    @Test
    public void givenAuthRequestOnPrivateEndpoint_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/health/private").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
