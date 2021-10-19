//package net.guides.springboot2.springboot2webappjsp;
//
//import com.auth0.jwt.interfaces.Claim;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import net.guides.springboot2.springboot2webappjsp.configuration.JwtInterceptor;
//import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
//import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
//import net.guides.springboot2.springboot2webappjsp.domain.User;
//import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.util.DigestUtils;
//import com.auth0.jwt.JWT;
//import javax.servlet.http.HttpServletRequest;
//import java.io.UnsupportedEncodingException;
//import static org.mockito.Mockito.when;
//
//import org.powermock.modules.junit4.PowerMockRunner;
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({JwtUtil.class})
////@AutoConfigureMockMvc
////@PowerMockIgnore({"org.mockito.*"})
//public class UserProfileControllerTest {
//
//
//    @Autowired
//    private UserController uc;
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper mapper;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private User user1;
//
//    @MockBean
//    private MockHttpServletRequest request;
//
//    @MockBean
//    private MockHttpServletResponse response;
//
//    @MockBean
//    private JwtUtil jwtUtil;
//    @MockBean
//    private JWT jwt;
////
////    @MockBean
////    @Qualifier("JwtInterceptor.class")
////    private JwtInterceptor jwtInterceptor;
//
//    @MockBean
//    private DecodedJWT decodedJWT;
//    @MockBean
//    private Claim claim;
//
////    @Before
////    public void setup() throws UnsupportedEncodingException {
////        User user = new User();
////        user.setEmail("544433@qq.com");
////        user.setUsername("rick");
////        user.setPassword("543666");
////        when(userRepository.save(user)).thenReturn(user);
////        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
////
////
////    }
//
//
//    @Before
//    public void init(){
//        MockitoAnnotations.initMocks(this);
//
//
//    }
//
//
//
//    /**
//     * changeNameAndPassword part testing
//     * @throws Exception
//     */
//
//    @Test
//    public void testChangeNameAndPasswordCorrect() throws Exception{
////        skip token
//        Object o = new Object();
////        JwtInterceptor mock = mock(JwtInterceptor.class);
//       PowerMockito.when(mock.preHandle(request,response,o)).thenReturn(true);
////        Mockito.mock(JwtUtil.class);
//        PowerMockito.mockStatic(JwtUtil.class);
//        when(JwtUtil.getUserEmailByToken(request)).thenReturn("544433@qq.com");
//
//
//
//
//        User user1 = new User();
//        user1.setEmail("544433@qq.com");
//        user1.setUsername("rick");
//        user1.setPassword( DigestUtils.md5DigestAsHex("543666".getBytes()));
//
//        Mockito.when(userRepository.getUserByEmail("544433@qq.com")).thenReturn(user1);
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
//
//
//
//
//    @Test
//    public void testpreHandlerCorrect() throws Exception{
//
//
//        Object o = new Object();
//        Mockito.when(jwtInterceptor.preHandle(request,response,o)).thenReturn(true);
////        Mockito.when(user1.getPassword()).thenReturn("token");
////        String tok = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzQ2MTE4MzAsImVtYWlsIjoidG9rZW5AZ21haWwuY29tIn0.UJz9VplAXd3lzayQsayo0p-LcTw1A-t1rTsyM-B4JRE";
////        Mockito.when(request.getHeader("Authorization")).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzQ2MTE4MzAsImVtYWlsIjoidG9rZW5AZ21haWwuY29tIn0.UJz9VplAXd3lzayQsayo0p-LcTw1A-t1rTsyM-B4JRE");
////        Mockito.when(jwt.decodeJwt(tok)).thenReturn(decodedJWT);
//////        Mockito.when(JwtUtil.getUserEmailByToken(request)).thenReturn("token@gmail.com");
////        Mockito.when(decodedJWT.getClaim("email")).thenReturn(claim);
////        Mockito.when(claim.asString()).thenReturn("token@gmail.com");
//
//
//        Assert.assertEquals(true,jwtInterceptor.preHandle(request,response,o));
//
//
//    }
//
//
//
//}
