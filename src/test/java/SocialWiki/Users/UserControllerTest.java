package SocialWiki.Users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().value("user", "testUserName1"))
                .andExpect(cookie().maxAge("user", 86400))
                .andExpect(content().string(containsString("\"id\":")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotEquals("Failure - successful login did not create a session for user", null, session);

        // perform successful login with email
        result = mockMvc.perform(post("/login")
                .content("login=testEmail1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().value("user", "testUserName1"))
                .andExpect(cookie().maxAge("user", 86400))
                .andExpect(content().string(containsString("\"id\":")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotEquals("Failure - successful login did not create a session for user", null, session);

        // perform successful login with extra parameter
        result = mockMvc.perform(post("/login")
                .content("login=testEmail1&pass=testPassword1&last=notALastName")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().value("user", "testUserName1"))
                .andExpect(cookie().maxAge("user", 86400))
                .andExpect(content().string(containsString("\"id\":")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isOk())
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotEquals("Failure - successful login did not create a session for user", null, session);

        // perform unsuccessful login with an existing session
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());

        // perform unsuccessful login with missing "pass" parameter
        mockMvc.perform(post("/login")
                .content("login=testUserName1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        //perform unsuccessful login with missing "login" parameter
        mockMvc.perform(post("/login")
                .content("pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        //perform unsuccessful login with both parameters missing
        mockMvc.perform(post("/login")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with empty "login" parameter
        mockMvc.perform(post("/login")
                .content("login=&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with empty "pass" parameter
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with both parameters empty
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful login with wrong credentials
        mockMvc.perform(post("/login")
                .content("login=testUserName2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void create() throws Exception {
        // perform successful creation
        MvcResult result = mockMvc.perform(post("/signup")
                .content("user=testUserName2&first=testFirstName2&last=testLastName2&email=testEmail2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().value("user", "testUserName2"))
                .andExpect(cookie().maxAge("user", 86400))
                .andExpect(content().string(containsString("\"id\":")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName2\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName2\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName2\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail2\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotEquals("Failure - successful creation of new user did not create a session for user", null, session);

        User user = userRepo.findByUserName("testUserName2");
        assertNotEquals("Failure - userControllerTest did not successfully create new user", null, user);

        // perform unsuccessful creation with an existing session
        mockMvc.perform(post("/signup")
                .content("user=testUserName2&first=testFirstName2&last=testLastName2&email=testEmail2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());

        // perform unsuccessful creation with same userName
        mockMvc.perform(post("/signup")
                .content("user=testUserName2&first=testFirstName2&last=testLastName2&email=testEmail3&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isConflict());

        // perform unsuccessful creation with same email
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName2&last=testLastName2&email=testEmail2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isConflict());

        // perform unsuccessful creation with same userName and email
        mockMvc.perform(post("/signup")
                .content("user=testUserName2&first=testFirstName2&last=testLastName2&email=testEmail2&pass=testPassword2")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isConflict());

        // perform unsuccessful creation with missing userName parameter
        mockMvc.perform(post("/signup")
                .content("first=testFirstName3&last=testLastName3&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with empty userName parameter
        mockMvc.perform(post("/signup")
                .content("user=&first=testFirstName3&last=testLastName3&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with missing firstName parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&last=testLastName3&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with empty firstName parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=&last=testLastName3&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with missing lastName parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with empty lastName parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&last=&email=testEmail3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with missing email parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&last=testLastName3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with empty email parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&last=testLastName3&email=&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with missing password parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&last=testLastName3&email=testEmail3")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with empty password parameter
        mockMvc.perform(post("/signup")
                .content("user=testUserName3&first=testFirstName3&last=testLastName3&email=testEmail3&pass=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful creation with no parameters
        mockMvc.perform(post("/signup")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void logout() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful logout with existing session
        mockMvc.perform(post("/logout")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().value("user", ""))
                .andExpect(cookie().maxAge("user", 0))
                .andExpect(status().isNoContent());

        // perform login to get session
        result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // perform unsuccessful logout with invalidated session
        session.invalidate();
        mockMvc.perform(post("/logout")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());

        // perform unsuccessful logout with no session
        mockMvc.perform(post("/logout")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUser() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful delete with existing session
        mockMvc.perform(delete("/deleteUser")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().value("user", ""))
                .andExpect(cookie().maxAge("user", 0))
                .andExpect(status().isNoContent());

        // perform unsuccessful delete with invalidated session
        mockMvc.perform(delete("/deleteUser")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());

        // perform unsuccessful delete with no session
        mockMvc.perform(delete("/deleteUser")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isForbidden());

        // perform unsuccessful login with deleted credentials
        mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(content().string(""))
                .andExpect(status().isUnauthorized());

        // perform unsuccessful creation with deleted userName
        mockMvc.perform(post("/signup")
                .content("user=testUserName1&first=testFirstName1&last=testLastName1&email=testEmail1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(cookie().doesNotExist("user"))
                .andExpect(status().isConflict());
    }
}