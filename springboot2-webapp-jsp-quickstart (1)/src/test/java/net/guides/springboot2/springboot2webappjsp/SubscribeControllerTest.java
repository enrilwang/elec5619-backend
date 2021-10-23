package net.guides.springboot2.springboot2webappjsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.controllers.SubscribeController;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscriptionTypeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
@EnableWebMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MockBeans({@MockBean(UserRepository.class), @MockBean(SubscribeRepository.class), @MockBean(SubscriptionTypeRepository.class), @MockBean(SubscriptionTypeRepository.class)})
@PrepareForTest({JwtUtil.class})
public class SubscribeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private SubscribeController subscribeController;

    @Autowired
    private UserController userController;

    @Autowired
    private SubscriptionTypeRepository subscriptionTypeRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private String auth;

    //A global used logged in user
    private final User userU = new User();

    private final String subscribePath = "/subscribe";

    @Before
    public void setup() throws Exception {
        //Mock Response Entity
        //User
        //User User
        userU.setUsername("test");
        userU.setEmail("test@test.com");
        userU.setId(1);
        userU.setAccountBalance(0.0f);
        userU.setIsCreator("user");
        userU.setPassword(DigestUtils.md5DigestAsHex((new String("test")).getBytes()));
        //Creator User
        User userC1 = new User();
        userC1.setId(2);
        userC1.setIsCreator("creator");

        //SubscribeType
        SubscriptionType subscriptionType = new SubscriptionType(1, 2, 5.0f, 10.1f, 20.22f);

        //Mock DB
        //User
        Mockito.when(this.userRepository.getUserByEmail("test@test.com")).thenReturn(userU);
        Mockito.when(this.userRepository.getUserById(1)).thenReturn(userU);
        Mockito.when(this.userRepository.getUserById(2)).thenReturn(userC1);
        //
        Mockito.when(this.subscriptionTypeRepository.getSubscriptionTypeBySubscriptionTypeId(1)).thenReturn(subscriptionType);

        //Mock Request
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        RequestBuilder request = MockMvcRequestBuilders.post("/login")
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            this.auth = new String(jsonResponse.getString("token"));
        } catch (Exception ignored) {

        }
    }

    @After
    public void rollback() {
        userU.setAccountBalance(0.0f);
    }

    //Post Single Test
    //Param tests
    @Test
    public void test01RequestParamMonthExisted() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
        Assert.assertEquals("Required int parameter 'month' is not present", response.getResponse().getErrorMessage());
    }

    @Test
    public void test02RequestBodyExisted() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .header("Authorization", this.auth)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    //Required json param existed test
    @Test
    public void test03CreatorIdNotSet() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Creator is not existed", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test04CreatorIdSetNegative() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(-1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Creator is not existed", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test05CreatorIdSetUser() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("This user you subscribed is not a creator", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test06SubscriptionTypeIdNotSet() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription definition is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test07SubscriptionTypeIdSetNegative() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(-1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription definition is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test08SubscriptionTypeIdSetNotExisted() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(100);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription definition is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test09SubscribeTypeNotSet() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription type is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test10SubscribeTypeSetNegative() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(-1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription type is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test11SubscribeTypeSetNotExisted() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(100);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Subscription type is not existed.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test12SubscribeMonthSetNegative() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "-1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Month number should be a positive integer.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test13SubscribeMonthSetNotExisted() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "101")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Are you sure to subscribe a century?", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    //Logic test
    @Test
    public void test14ActivatedAlreadyActivated() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        //Actually, multiple shuffled elements in List should be test.
        List<Subscribe> subscribeRes = new ArrayList<>();
        Subscribe subscribeResEle = subscribeReq.copy();
        subscribeResEle.setActivated(true);
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Already activated. No need to subscribe again", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test15AccountBalanceZero() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        subscribeRes.add(subscribeReq.copy());
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Account balance is not enough.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test16AccountNotEnough() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(0.1f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        subscribeRes.add(subscribeReq.copy());
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Account balance is not enough.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    //Update Error Test
    @Test
    public void test17SubscribeUpdateFailed() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        subscribeRes.add(subscribeReq.copy());
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(null);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Update subscribe information failed. Balance is rolled back.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test18AccountBalanceUpdateFailed() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        Subscribe subscribeResEle = subscribeReq.copy();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(null);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Update balance failed. But subscribe is updated.", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test19ASuccessUpdate() throws Exception {
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        RequestBuilder request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        Subscribe subscribeResEle = subscribeReq.copy();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        User userResponse = userU.copy();
        userResponse.setAccountBalance(userU.getAccountBalance() - 5.0f);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userU);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Subscribe Success", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    //Get Single Test
    @Test
    public void test20GetNoParamNoData() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            Assert.assertEquals("[]", jsonResponse.getString("data"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test21GetNoParamSuccess() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        Subscribe subscribeResEle = new Subscribe(1, true, 1, 1, 1, 1, 1, date, date);
        List<Subscribe> subscribeRes = new ArrayList<>();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserId(1)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            Assert.assertEquals(date, jsonResponseData.getString("timeStart"));
            Assert.assertEquals(date, jsonResponseData.getString("timeEnd"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test22GetBySubscribeIdNotSet() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            Assert.assertEquals("[]", jsonResponse.getString("data"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test23GetBySubscribeIdChar() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "LA")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void test24GetBySubscribeIdSetZero() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "0")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test25GetBySubscribeIdNegative() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "-1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No subscription information", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test26GetBySubscribeIdNotExisted() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "100")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No subscription information", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test27GetBySubscribeIdSuccess() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("subscribeId", "1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        Subscribe subscribeRes = new Subscribe(1, true, 1, 1, 1, 1, 1, date, date);
        Mockito.when(subscribeRepository.getSubscribeBySubscribeId(1)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            Assert.assertEquals(date, jsonResponseData.getString("timeStart"));
            Assert.assertEquals(date, jsonResponseData.getString("timeEnd"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test28GetByUserIdNotSet() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            Assert.assertEquals("[]", jsonResponse.getString("data"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test29GetByUserIdChar() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "LA")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void test30GetByUserIdSetZero() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "0")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test31GetByUserIdNegative() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "-1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No subscription information", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test32GetByUserIdNotExisted() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "100")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No subscription information", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test33GetByUserIdSuccess() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .param("userId", "1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        Subscribe subscribeResEle = new Subscribe(1, true, 1, 1, 1, 1, 1, date, date);
        List<Subscribe> subscribeRes = new ArrayList<>();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserId(1)).thenReturn(subscribeRes);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            Assert.assertEquals(date, jsonResponseData.getString("timeStart"));
            Assert.assertEquals(date, jsonResponseData.getString("timeEnd"));
        } catch (Exception ignored) {

        }
    }

    //Post and Get
    @Test
    public void test34PostGetCombine() throws Exception {
        //Get First
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            Assert.assertEquals("[]", jsonResponse.getString("data"));
        } catch (Exception ignored) {

        }

        //Then Post
        //Mock Request
        Subscribe subscribeReq = new Subscribe();
        subscribeReq.setCreatorId(2);
        subscribeReq.setSubscriptionTypeId(1);
        subscribeReq.setSubscribeType(1);
        request = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReq))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        Subscribe subscribeResEle = subscribeReq.copy();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReq.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        User userResponse = userU.copy();
        userResponse.setAccountBalance(userU.getAccountBalance() - 5.0f);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userU);
        //Mock API Call
        response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Subscribe Success", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }

        //Get Again
        //Mock Request
        request = MockMvcRequestBuilders.get(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            //check valid period
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date start = sdf.parse(jsonResponseData.getString("timeStart"));
                Date end = sdf.parse(jsonResponseData.getString("timeEnd"));
                Assert.assertEquals(start.getYear(), end.getYear());
                Assert.assertEquals(start.getMonth()-1, end.getMonth());
                Assert.assertEquals(start.getDay(), end.getDay());
                Assert.assertEquals(start.getHours(), end.getHours());
                Assert.assertEquals(start.getMinutes(), end.getMinutes());
                Assert.assertEquals(start.getSeconds(), end.getSeconds());
                Assert.assertEquals(start.getTimezoneOffset(), end.getTimezoneOffset());
            }catch (Exception e){
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeStart"));
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeEnd"));
            }
        } catch (Exception ignored) {

        }
    }

    //Delete Single
    @Test
    public void test35DeleteNoParam() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
            Assert.assertEquals("Required int parameter 'creatorId' is not present", response.getResponse().getErrorMessage());
    }

    @Test
    public void test36DeleteCreatorIdNotSet() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void test37DeleteCreatorIdChar() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","LA")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        mockMvc.perform(request)
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    public void test38DeleteCreatorIdNegative() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","-1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No activated subscribe", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test39DeleteCreatorIdNotExisted() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","100")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No activated subscribe", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test40DeleteCreatorIdUser() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","1")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("No activated subscribe", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    @Test
    public void test41DeleteCreatorIdSuccess() throws Exception {
        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId","2")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        Subscribe subscribeResEle = new Subscribe();
        subscribeResEle.setActivated(true);
        List<Subscribe> subscribesRes=new ArrayList<>();
        subscribesRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), 2,true)).thenReturn(subscribesRes);
        Subscribe subscribeResultEle = new Subscribe();
        subscribeResultEle.setActivated(false);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResultEle);
        //Mock API Call
        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Now is inactivated", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }
    }

    //Post Get & Delete Combine Test
    @Test
    public void test42PostGetDeleteCombine() throws Exception {
        //Get First
        //Mock Request
        RequestBuilder requestGet = MockMvcRequestBuilders.get(subscribePath)
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(requestGet)
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            Assert.assertEquals("[]", jsonResponse.getString("data"));
        } catch (Exception ignored) {

        }

        //Then Post
        //Mock Request
        Subscribe subscribeReqPost = new Subscribe();
        subscribeReqPost.setCreatorId(2);
        subscribeReqPost.setSubscriptionTypeId(1);
        subscribeReqPost.setSubscribeType(1);
        RequestBuilder requestPost = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReqPost))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        List<Subscribe> subscribeRes = new ArrayList<>();
        Subscribe subscribeResEle = subscribeReqPost.copy();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReqPost.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        User userResponse = userU.copy();
        userResponse.setAccountBalance(userU.getAccountBalance() - 5.0f);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userU);
        //Mock API Call
        response = mockMvc.perform(requestPost)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Subscribe Success", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }

        //Get Again
        //Mock API Call
        response = mockMvc.perform(requestGet)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            //check valid period
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date start = sdf.parse(jsonResponseData.getString("timeStart"));
                Date end = sdf.parse(jsonResponseData.getString("timeEnd"));
                Assert.assertEquals(start.getYear(), end.getYear());
                Assert.assertEquals(start.getMonth()-1, end.getMonth());
                Assert.assertEquals(start.getDay(), end.getDay());
                Assert.assertEquals(start.getHours(), end.getHours());
                Assert.assertEquals(start.getMinutes(), end.getMinutes());
                Assert.assertEquals(start.getSeconds(), end.getSeconds());
                Assert.assertEquals(start.getTimezoneOffset(), end.getTimezoneOffset());
            }catch (Exception e){
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeStart"));
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeEnd"));
            }
        } catch (Exception ignored) {

        }

        //Then try post again, should fail
        //Mock API Call
        subscribeRes = new ArrayList<>();
        subscribeResEle = subscribeReqPost.copy();
        subscribeResEle.setActivated(true);
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReqPost.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        response = mockMvc.perform(requestPost)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("1", jsonResponse.getString("code"));
            Assert.assertEquals("Already activated. No need to subscribe again", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }

        //Then Delete
        //Mock Request
        RequestBuilder requestDelete = MockMvcRequestBuilders.delete(subscribePath)
                .param("creatorId", "2")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock Response
        subscribeResEle = new Subscribe();
        subscribeResEle.setActivated(true);
        List<Subscribe> subscribesRes=new ArrayList<>();
        subscribesRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), 2,true)).thenReturn(subscribesRes);
        Subscribe subscribeResultEle = new Subscribe();
        subscribeResultEle.setActivated(false);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResultEle);
        //Mock API Call
        response = mockMvc.perform(requestDelete)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Now is inactivated", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }

        //Get Again
        //Mock API Call
        response = mockMvc.perform(requestGet)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            //check valid period
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date start = sdf.parse(jsonResponseData.getString("timeStart"));
                Date end = sdf.parse(jsonResponseData.getString("timeEnd"));
                Assert.assertEquals(start.getYear(), end.getYear());
                Assert.assertEquals(start.getMonth()-1, end.getMonth());
                Assert.assertEquals(start.getDay(), end.getDay());
                Assert.assertEquals(start.getHours(), end.getHours());
                Assert.assertEquals(start.getMinutes(), end.getMinutes());
                Assert.assertEquals(start.getSeconds(), end.getSeconds());
                Assert.assertEquals(start.getTimezoneOffset(), end.getTimezoneOffset());
            }catch (Exception e){
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeStart"));
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeEnd"));
            }
        } catch (Exception ignored) {

        }

        //Then Post
        //Mock Request
        subscribeReqPost = new Subscribe();
        subscribeReqPost.setCreatorId(2);
        subscribeReqPost.setSubscriptionTypeId(1);
        subscribeReqPost.setSubscribeType(1);
        requestPost = MockMvcRequestBuilders.post(subscribePath)
                .param("month", "1")
                .content(mapper.writeValueAsString(subscribeReqPost))
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        userU.setAccountBalance(2000.0f);
        //Mock Response
        subscribeRes = new ArrayList<>();
        subscribeResEle = subscribeReqPost.copy();
        subscribeRes.add(subscribeResEle);
        Mockito.when(subscribeRepository.getSubscribeByUserIdAndCreatorIdAndActivated(userU.getId(), subscribeReqPost.getCreatorId(), true)).thenReturn(subscribeRes);
        Mockito.when(subscribeRepository.save(Mockito.any(Subscribe.class))).thenReturn(subscribeResEle);
        userResponse = userU.copy();
        userResponse.setAccountBalance(userU.getAccountBalance() - 5.0f);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userU);
        //Mock API Call
        response = mockMvc.perform(requestPost)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Subscribe Success", jsonResponse.getString("msg"));
        } catch (Exception ignored) {

        }

        //Get Again
        //Mock API Call
        response = mockMvc.perform(requestGet)
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        try {
            Assert.assertEquals("0", jsonResponse.getString("code"));
            Assert.assertEquals("Success!", jsonResponse.getString("msg"));
            JSONObject jsonResponseData = new JSONObject(response.getResponse().getContentAsString());
            Assert.assertEquals("1", jsonResponseData.getString("subscribeId"));
            Assert.assertEquals("true", jsonResponseData.getString("activated"));
            Assert.assertEquals("1", jsonResponseData.getString("creatorId"));
            Assert.assertEquals("1", jsonResponseData.getString("paymentsSerialId"));
            Assert.assertEquals("1", jsonResponseData.getString("subscribeType"));
            Assert.assertEquals("1", jsonResponseData.getString("subscriptionTypeId"));
            Assert.assertEquals("1", jsonResponseData.getString("userId"));
            //check valid period
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                Date start = sdf.parse(jsonResponseData.getString("timeStart"));
                Date end = sdf.parse(jsonResponseData.getString("timeEnd"));
                Assert.assertEquals(start.getYear(), end.getYear());
                Assert.assertEquals(start.getMonth()-1, end.getMonth());
                Assert.assertEquals(start.getDay(), end.getDay());
                Assert.assertEquals(start.getHours(), end.getHours());
                Assert.assertEquals(start.getMinutes(), end.getMinutes());
                Assert.assertEquals(start.getSeconds(), end.getSeconds());
                Assert.assertEquals(start.getTimezoneOffset(), end.getTimezoneOffset());
            }catch (Exception e){
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeStart"));
                Assert.assertEquals("Date Format Error!!", jsonResponseData.getString("timeEnd"));
            }
        } catch (Exception ignored) {

        }
    }
}

