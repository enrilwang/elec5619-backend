package net.guides.springboot2.springboot2webappjsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.springboot2webappjsp.controllers.Result;
import net.guides.springboot2.springboot2webappjsp.controllers.SubscribeController;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import  org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration

@AutoConfigureMockMvc
public class SubscribeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController uc;

    @Autowired
    private SubscribeRepository subscribeRepo;

//    @Autowired
//    private ToDoService


    ObjectMapper mapper = new ObjectMapper();

    String auth;
    final String subscribePath = "/subscribe";
    final String loginPath = "/login";

    @Before
    public void getAuth() throws Exception {
        Map<String, Object> json = new HashMap<>();
        json.put("email", "test1@test.com");
        json.put("password", "enn");
        RequestBuilder request = MockMvcRequestBuilders.post(loginPath)
                .content(mapper.writeValueAsString(json))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult response = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse=new JSONObject(response.getResponse().getContentAsString());
        try{
            this.auth=jsonResponse.getString("token");
        }catch (Exception e){

        }
    }

    @Test
    public void testCheckBalance1() throws Exception {
        System.out.println(this.auth);
        Subscribe json =new Subscribe();
        json.setCreatorId(-1);
        json.setSubscribeId(-1);
        json.setSubscribeType(-1);
//        Mockito.when(subscribeRepo.save(json)).thenReturn(json);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .content(mapper.writeValueAsString(json))
                .header("Authorization",this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        MvcResult response = mvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
//        String responseString =response.getResponse().getContentAsString();
//        JSONObject jsonResponse=null;
//        try{
//            jsonResponse=new JSONObject(responseString);
//        }catch (Exception e){
//
//        }

//        if (!responseString.isEmpty()){
//            jsonResponse=new JSONObject(responseString);
//        }
//        System.out.println(jsonResponse);
//        System.out.println("==");
//        System.out.println(response.getResponse().getContentAsString());
//        System.out.println(new JSONObject(responseString));
//        Assert.assertEquals("1", jsonResponse.getString("code"));
//        Assert.assertEquals("Creator is not existed", jsonResponse.getString("msg"));
//        Assert.assertEquals("Required int parameter 'month' is not present",jsonResponse.getString("msg"));
//        Assert.assertEquals("[]", jsonResponse.getString("data"));




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

