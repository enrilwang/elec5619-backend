package net.guides.springboot2.springboot2webappjsp;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.guides.springboot2.springboot2webappjsp.domain.Artifact;
import net.guides.springboot2.springboot2webappjsp.domain.Category;
import net.guides.springboot2.springboot2webappjsp.domain.Subscribe;
import net.guides.springboot2.springboot2webappjsp.domain.User;
import net.guides.springboot2.springboot2webappjsp.repositories.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@MockBeans({@MockBean(UserRepository.class), @MockBean(ArtifactRepository.class), @MockBean(CategoryRepository.class)})
public class ArtifactControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ArtifactRepository artifactRepo;
    @Autowired
    private CategoryRepository categoryRepo;

    private User testUser = new User();
    private Category testCategory = new Category();
    private Artifact testWork = new Artifact();

    private final ObjectMapper mapper = new ObjectMapper();
    private String auth;

    @Before
    public void before() throws Exception {

        testUser.setEmail("test@test.com");
        testUser.setId(0);
        testUser.setIsCreator("creator");
        testUser.setName("test creator");
        testUser.setPassword(DigestUtils.md5DigestAsHex(("test").getBytes()));

        testCategory.setId("General");

        testWork.setUser(testUser);
        testWork.setArtifactId(0);
        testWork.setArtifactWeights(0);
        testWork.setDescription("World");
        testWork.setTitle("Hello");
        testWork.setStoreLocation("api/post/test.gif");
        testWork.setCategoryName(testCategory);


        List<Artifact> testList = new ArrayList<>();
        testList.add(testWork);

        //Mock DB
        Mockito.when(this.userRepo.getUserByEmail("test@test.com")).thenReturn(testUser);
        Mockito.when(this.userRepo.getUserById(0)).thenReturn(testUser);
        Mockito.when(this.artifactRepo.findAll()).thenReturn(testList);
        Mockito.when(this.userRepo.save(testUser)).thenReturn(testUser);
        Mockito.when(this.artifactRepo.save(testWork)).thenReturn(testWork);


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
            this.auth = jsonResponse.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * Method: getWorkById(HttpServletRequest request)
     *
     */
    @Test
    public void testGetWorkById() throws Exception {

        //test query object
        List<Map<String,Object>> artifactTest = new ArrayList<>();
        HashMap<String, Object> artifact = new HashMap<>();
        artifact.put("artifact_id", 0);
        artifact.put("title", "Hello");
        artifact.put("artifact_weights", 0);
        artifact.put("description", "World");
        artifact.put("store_location", "api/post/test.gif");
        artifactTest.add(0, artifact);

        Mockito.when(this.artifactRepo.findByUserId(0)).thenReturn(artifactTest);

        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.get("/works")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request).andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        Assert.assertEquals("0", jsonResponse.getString("code"));
        Assert.assertEquals("Query success!", jsonResponse.getString("msg"));

    }

    /**
     *
     * Method: deleteArtifact(@PathVariable("work_id") Integer work_id)
     *
     */
    @Test
    public void testDeleteArtifact() throws Exception {

        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.delete("/works/del/0")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request).andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        Assert.assertEquals("0", jsonResponse.getString("code"));
        Assert.assertEquals("Delete success!", jsonResponse.getString("msg"));

    }

    /**
     *
     * Method: addArtifact(HttpServletRequest request,
     * @RequestParam (value = "file", required = false) MultipartFile file,
     * @RequestParam Integer weight,
     * @RequestParam String title,
     * @RequestParam (required = false) String description,
     * @RequestParam String category_name)
     *
     */
    @Test
    public void testAddArtifact() throws Exception {

        Mockito.when(this.categoryRepo.findById("General")).thenReturn(Optional.of(testCategory));

        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.post("/works/add")
                .param("title", "Hello World!")
                .param("weight", String.valueOf(0))
                .param("category_name", "General")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request).andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        Assert.assertEquals("0", jsonResponse.getString("code"));
        Assert.assertEquals("Post success!", jsonResponse.getString("msg"));


    }

    /**
     *
     * Method: updateArtifact(@PathVariable(value = "work_id") Integer work_id,
     * @RequestParam Integer weight,
     * @RequestParam String title,
     * @RequestParam(required = false) String description,
     * @RequestParam String category_name)
     *
     */
    @Test
    public void testUpdateArtifactIdNotExist() throws Exception {
        Mockito.when(this.artifactRepo.findArtifactByArtifactId(0)).thenReturn(testWork);
        Mockito.when(this.categoryRepo.findById("General")).thenReturn(Optional.of(testCategory));

        //Mock Request
        RequestBuilder request = MockMvcRequestBuilders.post("/works/edit/123")
                .param("weight", String.valueOf(1))
                .param("title", "World")
                .param("description", "Hello")
                .param("category_name", "general")
                .header("Authorization", this.auth)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON);
        //Mock API Call
        MvcResult response = mockMvc.perform(request).andReturn();

        JSONObject jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        Assert.assertEquals("1", jsonResponse.getString("code"));
        Assert.assertEquals("Request artifact not exist!", jsonResponse.getString("msg"));

    }

}
