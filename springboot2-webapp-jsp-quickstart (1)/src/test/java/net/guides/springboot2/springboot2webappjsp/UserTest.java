package net.guides.springboot2.springboot2webappjsp;


import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;


public class UserTest extends BaseTest{

    @Autowired
    private UserController uc;

    @MockBean
    private UserRepository userRepository;

    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;


    @Before
    public void setup() {
        User user = new User();
        user.setEmail("544433@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

//    @Test
//    public void testLogin() throws Exception{
//        User user = new User();
//
////       mockMvc.perform(MockMvcBuilder.post("/api/login").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    }

    @Test
    public void testUserFindByEmail() throws Exception{



        User find = userRepository.getUserByEmail("544433@qq.com");
        Assert.assertEquals("rick",find.getUsername());
    }



}
