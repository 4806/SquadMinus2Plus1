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
import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Connor on 2017-03-08.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
        User user = new User("testUserName", "testFirstName", "testLastName", "Test@email.com", "testPassword");
        user = userRepo.save(user);

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
        User user1 = new User("testUserName", "testFirstName", "testLastName", "Test@email.com", "testPassword");
        user1 = userRepo.save(user1);
        ConcreteWikiPage page1 = new ConcreteWikiPage("testTitle1", "testContent1", user1);
        page1 = pageRepo.save(page1);

        user1.likePage(page1);
        user1 = userRepo.save(user1);


        // login to get the request
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        HttpServletRequest request = result.getRequest();

        Cookie c = CookieManager.getIsLikedCookie(user1, page1.getId());
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "true", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

        MockHttpSession session = (MockHttpSession) request.getSession(false);

        // unlike page
        mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session));

        c = CookieManager.getIsLikedCookie(user1, page1.getId());
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "false", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

    }

    @Test
    public void getClearIsLikedCookie() throws Exception {
        Cookie c = CookieManager.getClearIsLikedCookie();
        assertEquals("Failure - created cookie does not have the correct name", "isLiked", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 0, c.getMaxAge());
    }

    @Test
    public void getIsFollowedCookie() throws Exception {
        // set up user following a user
        User user1 = new User("testUserName", "testFirstName", "testLastName", "Test1@email.com", "testPassword");
        user1 = userRepo.save(user1);
        User user2 = new User("testUserName2", "testFirstName2", "testLastName2", "Test2@email.com", "testPassword2");
        user2 = userRepo.save(user2);

        user1.followUser(user2);
        user1 = userRepo.save(user1);


        // login to get the request
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName&pass=testPassword")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        HttpServletRequest request = result.getRequest();

        Cookie c = CookieManager.getIsFollowedCookie(user1, user2.getUserName());
        assertEquals("Failure - created cookie does not have the correct name", "isFollowed", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "true", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

        MockHttpSession session = (MockHttpSession) request.getSession(false);

        // unfollow user
        result = mockMvc.perform(post("/unfollowUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andReturn();

        request = result.getRequest();
        c = CookieManager.getIsFollowedCookie(user1, user2.getUserName());
        assertEquals("Failure - created cookie does not have the correct name", "isFollowed", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "false", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 86400, c.getMaxAge());

    }

    @Test
    public void getClearIsFollowedCookie() throws Exception {
        Cookie c = CookieManager.getClearIsFollowedCookie();
        assertEquals("Failure - created cookie does not have the correct name", "isFollowed", c.getName());
        assertEquals("Failure - created cookie does not have the correct value", "", c.getValue());
        assertEquals("Failure - created cookie does not have the correct maxAge", 0, c.getMaxAge());
    }

    @After
    public void tearDown() throws Exception {
        pageRepo.deleteAll();
        userRepo.deleteAll();
    }

}