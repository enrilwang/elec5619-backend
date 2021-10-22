package net.guides.springboot2.springboot2webappjsp;

import net.guides.springboot2.springboot2webappjsp.configuration.JwtInterceptor;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.powermock.modules.junit4.PowerMockRunnerDelegate;
//import org.powermock.modules.junit4.rule.PowerMockRule;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;



@AutoConfigureMockMvc
@SpringBootTest
public class UserProfileControllerTest extends BaseTest{



    @MockBean
    private MockMvc mockMvc;
////    @Autowired
////    private ObjectMapper mapper;
////
////    @Autowired
////    private UserRepository userRepository;
//    @injectMock
//    private UserRepository userRepository;
    @MockBean
    private User
//    @Mock
//    private User user1;

    @MockBean
    private MockHttpServletRequest request;

    @MockBean
    private MockHttpServletResponse response;

//
    @MockBean
    @Qualifier("JwtInterceptor.class")
    private JwtInterceptor jwtInterceptor;
////
//    @Rule
//    public PowerMockRule rule = new PowerMockRule();
//    @Before
//    public void setup() throws UnsupportedEncodingException {
//        User user = new User();
//        user.setEmail("544433@qq.com");
//        user.setUsername("rick");
//        user.setPassword("543666");
//        when(userRepository.save(user)).thenReturn(user);
//        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
//
//
//    }


    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);

//        PowerMockito.mockStatic(JwtUtil.class);
//        Mockito.mockStat
    }



    /**
     * changeNameAndPassword part testing
     * @throws Exception
     */

//    @Test
//    public void testChangeNameAndPasswordCorrect() throws Exception{
////        skip token
//        User user1 = new User();
//        user1.setEmail("544433@qq.com");
//        user1.setUsername("rick");
//        user1.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));
//
//        User user2 = new User();
//        user2.setEmail("111@qq.com");
//        user2.setUsername("rick");
//        user2.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));
//
//
//        Object o = new Object();
//        when(jwtInterceptor.preHandle(request,response,o)).thenReturn(true);
//
//
//        when(JwtUtil.getUserEmailByToken(request)).thenReturn("111@qq.com");
//        Mockito.when(userRepository.getUserByEmail("111@qq.com")).thenReturn(user2);
//
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/changeNameAndPassword").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(user1));
//        MvcResult result = mockMvc.perform(mockRequest).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//
//        String[] ans = response.getContentAsString().split(",");
//        //String expected = "{\"email\":\"544433@qq.com\",\"password\":\"543666\"}";
//        Assert.assertEquals("\"msg\":\"login successfully!\"", ans[0]);
//
//
//    }

    /**
     * change role part testing
     * @throws Exception
     */

    @Test
    public void testChangeRoleCorrect() throws Exception {



//        skip token
        User user1 = new User();
        user1.setEmail("544433@qq.com");
        user1.setUsername("rick");
        user1.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));
//
//        try(MockedStatic<JwtUtil> jwtMocked = Mockito.mockStatic(JwtUtil.class)) {
//            jwtMocked.when(()-> JwtUtil.getUserEmailByToken(request)).thenReturn("54333@qq.com");
//        }


//        Object o = new Object();
//        when(jwtInterceptor.preHandle(request,response,o)).thenReturn(true);
//
        PowerMockito.mockStatic(JwtUtil.class);
        PowerMockito.when(JwtUtil.getUserEmailByToken(request)).thenReturn("54333@163.com");

//        when(JwtUtil.getUserEmailByToken(request)).thenReturn("111@qq.com");
//        Mockito.when(userRepository.getUserByEmail("111@qq.com")).thenReturn(user1);


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/changeRole").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(String.valueOf((user1)));
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();




        String[] ans = response.getContentAsString().split(",");
        //String expected = "{\"email\":\"544433@qq.com\",\"password\":\"543666\"}";
        Assert.assertEquals("\"msg\":\"login successfully!\"", ans[0]);


    }



    @Test
    public void testpreHandlerCorrect() throws Exception{


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object o = new Object();
        Mockito.when(jwtInterceptor.preHandle(request,response,o)).thenReturn(true);
        Assert.assertEquals(true,jwtInterceptor.preHandle(request,response,o));


    }



}
