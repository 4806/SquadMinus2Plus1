package SocialWiki.Cookies;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageRepository;
import org.junit.After;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Connor on 2017-03-08.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CookieManagerTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    WikiPageRepository pageRepo;

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
        userRepo.save(user);

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

    @Test
    public void getIsLikedCookie() throws Exception {
        // set up user liking a page
        User user1 = new User("testUserName", "testFirstName", "testLastName", "testEmail", "testPassword");
        ConcreteWikiPage page1 = new ConcreteWikiPage("testTitle1", "testContent1", user1);
        user1.likePage(page1);
        userRepo.save(user1);
        pageRepo.save(page1);

        // login to get the request
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        HttpServletRequest request = result.getRequest();

        Cookie c = CookieManager.getIsLikedCookie(request, page1.getId());
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "true", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

        MockHttpSession session = (MockHttpSession) request.getSession(false);

        // unlike page
        result = mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andReturn();

        request = result.getRequest();
        c = CookieManager.getIsLikedCookie(request, page1.getId());
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "false", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

        // invalidate session
        session = (MockHttpSession) request.getSession(false);
        session.invalidate();

        c = CookieManager.getIsLikedCookie(request, page1.getId());
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 0, c.getMaxAge());
    }

    @After
    public void tearDown() throws Exception {
        pageRepo.deleteAll();
        userRepo.deleteAll();
    }

}