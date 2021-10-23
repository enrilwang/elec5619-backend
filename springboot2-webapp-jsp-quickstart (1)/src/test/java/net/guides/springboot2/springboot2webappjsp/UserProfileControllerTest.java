package net.guides.springboot2.springboot2webappjsp;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.guides.springboot2.springboot2webappjsp.configuration.JwtUtil;
import net.guides.springboot2.springboot2webappjsp.controllers.SubscribeController;
import net.guides.springboot2.springboot2webappjsp.controllers.UserController;
import net.guides.springboot2.springboot2webappjsp.controllers.UserProfileController;
import net.guides.springboot2.springboot2webappjsp.domain.Artifact;
import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.ArtifactRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscribeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.SubscriptionTypeRepository;
import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
@EnableWebMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MockBeans({@MockBean(UserRepository.class), @MockBean(SubscribeRepository.class), @MockBean(SubscriptionTypeRepository.class), @MockBean(SubscriptionTypeRepository.class), @MockBean(ArtifactRepository.class)})
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ArtifactRepository artifactRepository;



    private final ObjectMapper mapper = new ObjectMapper();

    private String auth;

    //A global used logged in user
    private final User userU = new User();

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
        userU.setFavoriteId("[1, 2]");
        userU.setSubscribeId("[1, 2]");


        User userU1 = new User();
        userU1.setId(2);
        userU1.setIsCreator("user");


        List<Artifact> arr = new ArrayList<>();
        Artifact a = new Artifact();
        arr.add(a);

        List<User> arr1 = new ArrayList<>();
        arr1.add(userU);
        //Mock DB
        //User
        Mockito.when(this.userRepository.getUserByEmail("test@test.com")).thenReturn(userU);
        Mockito.when(this.userRepository.getUserById(1)).thenReturn(userU);
        Mockito.when(this.userRepository.getUserById(2)).thenReturn(userU1);
        Mockito.when(this.artifactRepository.findAllArtifact(1)).thenReturn(arr);
        Mockito.when(this.userRepository.findAll()).thenReturn(arr1);


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



    /**
     * change role part testing
     * @throws Exception
     */

    @Test
    public void testChangeRoleCorrect() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/changeRole").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userU));
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"change role successfully\"", ans[1]);


    }

    @Test
    public void testChangeNameAndPasswordCorrect() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/changeNameAndPassword").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userU));
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"change name and password successfully\"", ans[1]);


    }



    @Test
    public void testGetUserInfoCorrect() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/getUserInfo").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"get okay\"", ans[1]);


    }



    @Test
    public void testaddProfilePicturePathNotCorrect() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",new byte['a']);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.fileUpload("/addProfilePicture").file(file).header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        //String expected = "{\"email\":\"544433@qq.com\",\"password\":\"543666\"}";
        Assert.assertEquals("\"msg\":\"File upload fail!\"", ans[1]);

    }
    @Test
    public void testGetFavouriteList() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/getFavouriteList").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"OK\"", ans[1]);


    }

    @Test
    public void testGetSubscribeList() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/getSubscribeList").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();


        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"OK！！\"", ans[1]);


    }

    @Test
    public void testDeleteFavouriteList() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/deleteFavouriteList").param("favouriteUserId","idisss=1").header("Authorization",this.auth).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(mockRequest).andReturn();
        MockHttpServletResponse response1 = result.getResponse();

        String[] ans = response1.getContentAsString().split(",");
        Assert.assertEquals("\"msg\":\"OK\"", ans[1]);

    }


}
