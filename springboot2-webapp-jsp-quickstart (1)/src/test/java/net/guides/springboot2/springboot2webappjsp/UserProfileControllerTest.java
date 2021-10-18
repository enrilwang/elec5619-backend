package net.guides.springboot2.springboot2webappjsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@AutoConfigureMockMvc
public class UserProfileControllerTest extends BaseTest {


    @Autowired
    private UserController uc;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MockHttpServletRequest request;

    @MockBean
    private MockHttpServletResponse response;

    @MockBean
    private JwtUtil jwtUtil;


//    @Before
//    public void setup() throws UnsupportedEncodingException {
//        User user = new User();
//        user.setEmail("544433@qq.com");
//        user.setUsername("rick");
//        user.setPassword("543666");
//        Mockito.when(userRepository.save(user)).thenReturn(user);
//        Mockito.when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
//
//
//    }

    /**
     * changeNameAndPassword part testing
     * @throws Exception
     */

    @Test
    public void testChangeNameAndPasswordCorrect() throws Exception{
        Mockito.when(JwtUtil.getUserEmailByToken(request)).thenReturn("544433@qq.com");
        //Mockito.when(request.getHeader("Authorization")).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzQzNjcwNTMsImVtYWlsIjoiMTIzMTEzQHFxLmNvbSJ9.-mzdIERaQF5z0SlEaf4VqzZ_kBxMR7hiMWlwQlE0G34");

//        User user = new User();
//        user.setEmail("544433@qq.com");
//        user.setUsername("rick");
//        user.setPassword("543666");

        User user1 = new User();
        user1.setEmail("544433@qq.com");
        user1.setUsername("rick");
        user1.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));

//        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.getUserByEmail("544433@qq.com")).thenReturn(user1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/changeNameAndPassword").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user1));
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String[] ans = response.getContentAsString().split(",");
        //String expected = "{\"email\":\"544433@qq.com\",\"password\":\"543666\"}";
        Assert.assertEquals("\"msg\":\"login successfully!\"", ans[0]);


    }


}
