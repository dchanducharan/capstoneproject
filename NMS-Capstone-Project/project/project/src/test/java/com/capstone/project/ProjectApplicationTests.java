package com.capstone.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc

public class ProjectApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetConfigEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getconfig"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
