package SocialWiki.Cookies;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Connor on 2017-03-08.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CookieManagerTest {

    @Autowired
    UserRepository repo;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getUserCookie() throws Exception {
        Cookie c = CookieManager.getUserCookie("testUserName");
        assertEquals("Failure - created cookie does not have the correct name", "user", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "testUserName", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());
    }

    @Test
    public void getClearUserCookie() throws Exception {
        Cookie c = CookieManager.getClearUserCookie();
        assertEquals("Failure - created cookie does not have the correct name", "user", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 0, c.getMaxAge());
    }

    @Test
    public void checkUserCookie() throws Exception {
        User user = new User("testUserName", "testFirstName", "testLastName", "test@email.com", "testPassword");
        repo.save(user);

        // perform successful check where cookie value matches session value
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded")
                .cookie(new Cookie("user", "testUserName")))
                .andReturn();

        assertTrue("Failure - user cookie check failed when it should have passed", CookieManager.checkUserCookie(result.getRequest()));

        // perform successful check where cookie does not exist
        result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        assertTrue("Failure - user cookie check failed when it should have passed", CookieManager.checkUserCookie(result.getRequest()));

        // perform unsuccessful check where cookie value does not match session value
        result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded")
                .cookie(new Cookie("user", "test")))
                .andReturn();

        assertFalse("Failure - user cookie check passed when it should have failed", CookieManager.checkUserCookie(result.getRequest()));

        // perform unsuccessful check where session is invalid
        result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        result.getRequest().getSession().invalidate();

        assertFalse("Failure - user cookie check passed when it should have failed", CookieManager.checkUserCookie(result.getRequest()));
    }

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
    }

}