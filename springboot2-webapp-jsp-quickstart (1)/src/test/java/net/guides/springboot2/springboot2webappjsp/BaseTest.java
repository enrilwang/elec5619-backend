package net.guides.springboot2.springboot2webappjsp;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration

public class BaseTest {
    @Before
    public void init() {
        System.out.println("Start testing!");
    }


    @After
    public void after() {
        System.out.println("testing completed!");
    }


    @Test
    public void contextLoads() {
    }

}
