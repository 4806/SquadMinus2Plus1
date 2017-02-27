package SocialWiki.Users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by connor on 2/24/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    private User user1;

    @Before
    public void setUp() throws Exception {
        user1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        userRepo.save(user1);
    }

    @After
    public void tearDown() throws Exception {
        userRepo.deleteAll();
    }

    @Test
    public void login() throws Exception {
        // perform successful login with userName
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk());

        // perform successful login with email
        mockMvc.perform(post("/login")
                .content("login=testEmail1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk());

        // perform successful login with extra parameter
        mockMvc.perform(post("/login")
                .content("login=testEmail1&pass=testPassword1&last=notALastName")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk());

        // perform unsuccessful login with missing "pass" parameter
        mockMvc.perform(post("/login")
                .content("login=testUserName1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        //perform unsuccessful login with missing "login" parameter
        mockMvc.perform(post("/login")
                .content("pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        //perform unsuccessful login with both parameters missing
        mockMvc.perform(post("/login")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with empty "login" parameter
        mockMvc.perform(post("/login")
                .content("login=&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with empty "pass" parameter
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with both parameters empty
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with wrong credentials
        mockMvc.perform(post("/login")
                .content("login=testUserName2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(""))
                .andExpect(status().isUnauthorized());
    }

}