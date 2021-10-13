//package net.guides.springboot2.springboot2webappjsp;
//
//
//import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
//import net.guides.springboot2.springboot2webappjsp.domain.User;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//
//public class UserTest extends BaseTest{
//
//    @Autowired
//    private UserController uc;
//    private WebApplicationContext webApplicationContext;
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @Test
//    public void testLogin() throws Exception{
//       mockMvc.perform(MockMvcBuilder.post("/api/login").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//    }
//
//}
