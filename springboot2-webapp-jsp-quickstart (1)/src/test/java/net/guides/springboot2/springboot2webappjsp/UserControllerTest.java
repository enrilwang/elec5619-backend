package net.guides.springboot2.springboot2webappjsp;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import net.guides.springboot2.springboot2webappjsp.sdk.GeetestConfig;
import net.guides.springboot2.springboot2webappjsp.sdk.GeetestLib;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.spy;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest{

    @Autowired
    private UserController uc;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

//    @Mock
//    private GeetestLib gtSdk = spy(new GeetestLib("","",true));


    @Before
    public void setup() throws UnsupportedEncodingException {
        User user = new User();
        user.setEmail("544433@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);


    }


    /**
     * Login part testing
     * @throws Exception
     */

    @Test
    public void testLoginCorrect() throws Exception{


        User user = new User();
        user.setEmail("544433@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");

        User user1 = new User();
        user1.setEmail("544433@qq.com");
        user1.setUsername("rick");
        user1.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String[] ans = response.getContentAsString().split(",");
        //String expected = "{\"email\":\"544433@qq.com\",\"password\":\"543666\"}";
        Assert.assertEquals("\"msg\":\"login successfully!\"", ans[1]);


    }


    @Test
    public void testLoginWithNoEmail() throws Exception{
        User user = new User();
        user.setEmail("544433@qq.com");
        user.setUsername("rick");

        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"Username and email_address and password cannot be null\"", ans[1] );

    }


    @Test
    public void testLoginWithNoPassword() throws Exception{
        User user = new User();

        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"Username and email_address and password cannot be null\"", ans[1] );


    }

    @Test
    public void testLoginWithWrongPassword() throws Exception{
        User user = new User();
        user.setEmail("544433@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"Wrong password", ans[1] );


    }


    @Test
    public void testLoginWithNoRegister() throws Exception{
        User user = new User();
        user.setEmail("33333@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
//        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"You have not registered", ans[1] );


    }

    /**
     * test register function
     * @throws Exception
     */

    @Test
    public void testRegisterCorrect() throws Exception{
        User user = new User();
        user.setEmail("1133333@qq.com");
        user.setUsername("TOM");
        user.setPassword("543666");


        Mockito.when(userRepository.save(user)).thenReturn(user);
        //Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"register successful\"", ans[1] );


    }

    @Test
    public void testRegisterNoPassword() throws Exception{
        User user = new User();
        user.setEmail("33333@qq.com");


        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"email_address and password cannot be null\"", ans[1] );


    }

    @Test
    public void testRegisterNoEmail() throws Exception{
        User user = new User();

        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"email_address and password cannot be null\"", ans[1] );


    }

    @Test
    public void testRegisterAgain() throws Exception{
        User user = new User();
        user.setEmail("33333@qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"This user has already register\"", ans[1] );


    }


    @Test
    public void testRegisterEmailFormatIncorrect() throws Exception{
        User user = new User();
        user.setEmail("33333qq.com");
        user.setUsername("rick");
        user.setPassword("543666");
        //Mockito.when(userRepository.save(user)).thenReturn(user);
        //Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"email format does not correct\"", ans[1] );


    }


    @Test
    public void testUserFindByEmail() throws Exception{

        User find = userRepository.getUserByEmail("544433@qq.com");
        Assert.assertEquals("rick",find.getUsername());
    }




    @Test
    public void testLogoutCorrect() throws Exception{
        User user = new User();
        user.setEmail("1133333@qq.com");
        user.setUsername("TOM");
        user.setPassword("543666");


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/userlogout").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user));

        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String[] ans = response.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"logout successfully! \"", ans[1] );


    }


}
