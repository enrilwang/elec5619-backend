package net.guides.springboot2.springboot2webappjsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtInterceptor;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.controllers.SubscribeController;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
@EnableWebMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MockBeans({@MockBean(UserRepository.class), @MockBean(SubscribeRepository.class)/*, @MockBean(JwtInterceptor.class)*/})
@PrepareForTest({JwtUtil.class})
public class SubscribeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private SubscribeController subscribeController;
    @Autowired
    private UserController userController;
//    @Autowired
//    @Qualifier("jwti")
//    private JwtInterceptor jwti;


//    @MockBean


    ObjectMapper mapper = new ObjectMapper();

    String auth;

    final String registerPath = "/register";
    final String loginPath = "/login";
    final String subscribePath = "/subscribe";

    @Before
    public void setup() throws Exception {
        //Request
        User userR = new User();
        userR.setUsername("test");
        userR.setEmail("test@test.com");
        //Response
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        userR.setPassword(DigestUtils.md5DigestAsHex((new String("test")).getBytes()));
        //Mock DB
        Mockito.when(this.userRepository.getUserByEmail("test@test.com")).thenReturn(userR);
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.post(loginPath)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            this.auth = new String(jsonResponse.getString("token"));
            System.out.println(this.auth);
        } catch (Exception e) {

        }

    }


    //register the first user "test@test.com"
//    @Test
    public void test01mockRegister() throws Exception {
        //Request
        User userR = new User();
        userR.setUsername("test");
        userR.setEmail("test@test.com");
        //Response
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        userR.setPassword(DigestUtils.md5DigestAsHex((new String("test")).getBytes()));
        //Mock DB
        Mockito.when(this.userRepository.getUserByEmail("test@test.com")).thenReturn(userR);
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.post(loginPath)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            this.auth = new String(jsonResponse.getString("token"));
            System.out.println(this.auth);
        } catch (Exception e) {

        }
    }

    @Test
    public void test03testCheckBalance1() throws Exception {
        System.out.println(this.auth);
        Subscribe subscribe = new Subscribe();
        subscribe.setCreatorId(-1);
        subscribe.setSubscribeId(-1);
        subscribe.setSubscribeType(-1);
        Mockito.when(subscribeRepository.save(subscribe)).thenReturn(subscribe);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .content(mapper.writeValueAsString(subscribe))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andReturn();


//


//        var authentication = HttpContext.GetOwinContext().Authentication;
        // 2、post提交一个user
//        request = post("/users/")
//                .param("id", "1")
//                .param("name", "测试大师")
//                .param("age", "20");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("success")));
//
//        // 3、get获取user列表，应该有刚才插入的数据
//        request = MockMvcRequestBuilders.get("/users/");
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"测试大师\",\"age\":20}]")));
//
//        // 4、put修改id为1的user
//        request = MockMvcRequestBuilders.put("/users/1")
//                .param("name", "测试终极大师")
//                .param("age", "30");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("success")));
//
//        // 5、get一个id为1的user
//        request = MockMvcRequestBuilders.get("/users/1");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"测试终极大师\",\"age\":30}")));
//
//        // 6、del删除id为1的user
//        request = MockMvcRequestBuilders.delete("/users/1");
//        mvc.perform(request)
//                .andExpect(content().string(equalTo("success")));
//
//        // 7、get查一下user列表，应该为空
//        request = MockMvcRequestBuilders.get("/users/");
//        mvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("[]")));
    }
}

