package SocialWiki.Users;

import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageRepository;
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
import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by connor on 2/24/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WikiPageRepository pageRepo;

    private User user1;
    private User user2;
    private User user3;
    private ConcreteWikiPage page1;
    private ConcreteWikiPage page2;

    @Before
    public void setUp() throws Exception {
        user1 = userRepo.save(new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1"));
        user2 = userRepo.save(new User("testUserName3", "testFirstName3", "testLastName3", "testEmail3", "testPassword3"));
        user3 = userRepo.save(new User("testUserName4", "testFirstName4", "testLastName4", "testEmail4", "testPassword4"));
        page1 = pageRepo.save(new ConcreteWikiPage("testTitle1", "testContent1", user1));
        page2 = pageRepo.save(new ConcreteWikiPage("testTitle2", "testContent2", user2));
    }

    @After
    public void tearDown() throws Exception {
        pageRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    public void likePage() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful liking of page
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the page is now in the User's liked pages list
        User testUser = userRepo.findByUserName("testUserName1");
        assertTrue("Failure - wiki page is not in the User's liked pages list", testUser.getLikedPages().contains(page1));

        // perform successful liking of a second page
        mockMvc.perform(post("/likePage")
                .content("id=" + page2.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that both pages are now in the User's liked pages list
        testUser = userRepo.findByUserName("testUserName1");
        assertTrue("Failure - wiki page is not in the User's liked pages list", testUser.getLikedPages().contains(page1));
        assertTrue("Failure - wiki page is not in the User's liked pages list", testUser.getLikedPages().contains(page2));

        // perform unsuccessful liking of page that is already liked
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful liking of page that does not exist
        mockMvc.perform(post("/likePage")
                .content("id=-1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful liking of page with id parameter that is not a Long
        mockMvc.perform(post("/likePage")
                .content("id=test")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful liking of page with empty id parameter
        mockMvc.perform(post("/likePage")
                .content("id=")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful liking of page with no parameters
        mockMvc.perform(post("/likePage")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful liking of page with invalidated session
        session.invalidate();
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful liking of page with no session
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());

        // perform login to get session
        result = mockMvc.perform(post("/login")
                .content("login=testUserName3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful liking of page that another user has already liked
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the page is in both of the Users' liked pages lists
        User testUser1 = userRepo.findByUserName("testUserName1");
        User testUser2 = userRepo.findByUserName("testUserName3");
        assertTrue("Failure - wiki page is not in the User's liked pages list", testUser1.getLikedPages().contains(page1));
        assertTrue("Failure - wiki page is not in the User's liked pages list", testUser2.getLikedPages().contains(page1));

    }

    @Test
    public void unlikePage() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful liking of page
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform successful unliking of page
        mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the page is not in the User's liked pages list
        User testUser = userRepo.findByUserName("testUserName1");
        assertFalse("Failure - wiki page is not in the User's liked pages list", testUser.getLikedPages().contains(page1));

        // perform unsuccessful unliking of page that is already unliked
        mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful unliking of page that does not exist
        mockMvc.perform(post("/unlikePage")
                .content("id=-1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unliking of page with id parameter that is not a Long
        mockMvc.perform(post("/unlikePage")
                .content("id=test")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unliking of page with empty id parameter
        mockMvc.perform(post("/unlikePage")
                .content("id=")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unliking of page with no parameters
        mockMvc.perform(post("/unlikePage")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unliking of page with invalidated session
        session.invalidate();
        mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful unliking of page with no session
        mockMvc.perform(post("/unlikePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void retrieveUser() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful liking of page
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform successful retrieval of user
        mockMvc.perform(get("/retrieveUser?user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("\"id\":")))
                .andExpect(content().string(containsString("\"userName\":\"testUserName1\"")))
                .andExpect(content().string(containsString("\"firstName\":\"testFirstName1\"")))
                .andExpect(content().string(containsString("\"lastName\":\"testLastName1\"")))
                .andExpect(content().string(containsString("\"email\":\"testEmail1\"")))
                .andExpect(content().string(containsString("\"password\":null")))
                .andExpect(content().string(containsString("\"title\":\"testTitle1\"")))
                .andExpect(status().isOk());

        // perform unsuccessful retrieval of user with empty parameter
        mockMvc.perform(get("/retrieveUser?user=")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful retrieval of user with no parameter
        mockMvc.perform(get("/retrieveUser")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful retrieval of user with userName that does not exist
        mockMvc.perform(get("/retrieveUser?user=testUserName5")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isUnprocessableEntity());

        // perform login to get session
        result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // delete the logged in user
        mockMvc.perform(delete("/deleteUser")
                .contentType("application/x-www-form-urlencoded")
                .session(session));

        // perform unsuccessful retrieval of user that is deleted
        mockMvc.perform(get("/retrieveUser?user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void followUser() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful following of user
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the user is now in the User's followed users list
        User testUser = userRepo.findByUserName("testUserName1");
        assertTrue("Failure - user is not in the User's followed users list", testUser.getFollowedUsers().contains(user2));

        // perform unsuccessful following of user that is already followed
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful following of user where user is one making request
        mockMvc.perform(post("/followUser")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful following of user that does not exist
        mockMvc.perform(post("/followUser")
                .content("user=badName")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful following of user with empty id parameter
        mockMvc.perform(post("/followUser")
                .content("user=")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful following of user with no parameters
        mockMvc.perform(post("/followUser")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful following of user with invalidated session
        session.invalidate();
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful following of user with no session
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());

        // perform login to get session for user2
        result = mockMvc.perform(post("/login")
                .content("login=testUserName3&pass=testPassword3")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful following of user1 by user2 (check if users can follow each other)
        mockMvc.perform(post("/followUser")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the user is now in the User's followed users list
        testUser = userRepo.findByUserName("testUserName3");
        assertTrue("Failure - user1 is not in the User2's followed users list", testUser.getFollowedUsers().contains(user1));

        session.invalidate();

        // perform login to get session for user3
        result = mockMvc.perform(post("/login")
                .content("login=testUserName4&pass=testPassword4")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful following of user1 by user3 (Test is more than 1 user can follow same user)
        mockMvc.perform(post("/followUser")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the user is now in the User's followed users list
        testUser = userRepo.findByUserName("testUserName4");
        assertTrue("Failure - user1 is not in the User3's followed users list", testUser.getFollowedUsers().contains(user1));

    }

    @Test
    public void unfollowUser() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=testUserName1&pass=testPassword1")
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // perform successful following of user
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform successful unfollowing of user
        mockMvc.perform(post("/unfollowUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // check that the user is not in the User's followed users list
        User testUser = userRepo.findByUserName("testUserName1");
        assertFalse("Failure - user is in the User's followed users list", testUser.getFollowedUsers().contains(user2));

        // perform unsuccessful unfollowing of user that is already unfollowed
        mockMvc.perform(post("/unfollowUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful unfollowing of user that does not exist
        mockMvc.perform(post("/unfollowUser")
                .content("user=badName")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unfollowing of user with empty id parameter
        mockMvc.perform(post("/unfollowUser")
                .content("user=")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unfollowing of user with no parameters
        mockMvc.perform(post("/unfollowUser")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful unfollowing of user with invalidated session
        session.invalidate();
        mockMvc.perform(post("/unfollowUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful unfollowing of user with no session
        mockMvc.perform(post("/unfollowUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUsersFollowing() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // successfully get list of following user with no user following (blank array)
        mockMvc.perform(get("/getUsersFollowing")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(content().string(containsString("[]")));

        // successfully get list of following users with no session
        mockMvc.perform(get("/getUsersFollowing")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("[]")));

        // have user 1 follow user 2
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // successfully get list of following user with only 1 user followed
        mockMvc.perform(get("/getUsersFollowing")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$.[0]", is(user1.getUserName())));

        // logout of user 1
        mockMvc.perform(post("/logout")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(cookie().value("user", ""))
                .andExpect(cookie().maxAge("user", 0))
                .andExpect(status().isNoContent());

        // login to user 3
        result = mockMvc.perform(post("/login")
                .content("login=" + user3.getUserName() + "&pass=" + user3.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        session = (MockHttpSession) result.getRequest().getSession(false);

        // have user 3 follow user 2
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // successfully get list of following user with >1 users following
        mockMvc.perform(get("/getUsersFollowing")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$.[0]", is(user1.getUserName())))
                .andExpect(jsonPath("$.[1]", is(user3.getUserName())));

        // unsuccessful query with empty parameter
        mockMvc.perform(get("/getUsersFollowing")
                .contentType("application/x-www-form-urlencoded")
                .content("user=")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // unsuccessful query with invalid parameter
        mockMvc.perform(get("/getUsersFollowing")
                .contentType("application/x-www-form-urlencoded")
                .content("user=badName")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // unsuccessful query with no parameter
        mockMvc.perform(get("/getUsersFollowing")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void getFollowingUsers() throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // successfully get list of followed user with no user followed (blank array)
        mockMvc.perform(get("/getFollowingUsers")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(content().string(containsString("[]")));

        // successfully get list of followed users with no session
        mockMvc.perform(get("/getFollowingUsers")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(content().string(containsString("[]")));

        // have user 1 follow user 2
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // successfully get list of followed user with only 1 user followed
        mockMvc.perform(get("/getFollowingUsers")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$.[0]", is(user2.getUserName())));


        // have user 1 follow user 3
        mockMvc.perform(post("/followUser")
                .content("user=" + user3.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // successfully get list of followed user with >1 users followed
        mockMvc.perform(get("/getFollowingUsers")
                .content("user=" + user1.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$.[0]", is(user2.getUserName())))
                .andExpect(jsonPath("$.[1]", is(user3.getUserName())));

        // unsuccessful query with empty parameter
        mockMvc.perform(get("/getFollowingUsers")
                .contentType("application/x-www-form-urlencoded")
                .content("user=")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // successful query with invalid parameter return empty list
        mockMvc.perform(get("/getFollowingUsers")
                .contentType("application/x-www-form-urlencoded")
                .content("user=badName")
                .session(session))
                .andExpect(content().string(containsString("[]")));

        // unsuccessful query with no parameter
        mockMvc.perform(get("/getFollowingUsers")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void getUserNotifications()throws Exception {
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        // successfully get list of notifications with no user notifications present (blank array)
        mockMvc.perform(get("/getUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(content().string(containsString("[]")));

        user1.addNotification("N1");
        user1 = userRepo.save(user1);

        // successfully get list of notifications with 1 user notifications present
        mockMvc.perform(get("/getUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$[0]", is("N1")));

        user1.addNotification("N2");
        user1 = userRepo.save(user1);

        // successfully get list of notifications with 2 user notifications present
        mockMvc.perform(get("/getUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(jsonPath("$[0]", is("N1")))
                .andExpect(jsonPath("$[1]", is("N2")));

        // perform unsuccessful query with invalidated session
        session.invalidate();
        mockMvc.perform(get("/getUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful query with no session
        mockMvc.perform(get("/getUserNotifications")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void removeUserNotifications()throws Exception {

        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        user1.addNotification("N1");
        user1 = userRepo.save(user1);

        // successfully remove notifications with 1 user notifications present
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        user1.addNotification("N1");
        user1.addNotification("N2");
        user1 = userRepo.save(user1);

        // successfully remove notifications with 2 user notifications present
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());


        // perform unsuccessful removal when notification is not in list
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        user1 = userRepo.findOne(user1.getId());
        assertTrue("Failure - removal of notification did not remove right notification", user1.getNotifications().get(0).equals("N2"));

        // Remove other notifications
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N2")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform unsuccessful removal when no notifications present
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful query with empty parameter
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful query with no parameters
        mockMvc.perform(post("/removeUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isUnprocessableEntity());

        // perform unsuccessful query with invalidated session
        session.invalidate();
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful query with no session
        mockMvc.perform(post("/removeUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void removeAllUserNotifications()throws Exception {

        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        user1.addNotification("N1");
        user1 = userRepo.save(user1);

        // successfully remove notifications with 1 user notifications present
        mockMvc.perform(post("/removeAllUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        user1.addNotification("N1");
        user1.addNotification("N2");
        user1 = userRepo.save(user1);

        // successfully remove notifications with 2 user notifications present
        mockMvc.perform(post("/removeAllUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform successful removal when no notifications present
        mockMvc.perform(post("/removeAllUserNotifications")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        // perform unsuccessful query with invalidated session
        session.invalidate();
        mockMvc.perform(post("/removeAllUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isForbidden());

        // perform unsuccessful query with no session
        mockMvc.perform(post("/removeAllUserNotifications")
                .content("notification=N1")
                .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isForbidden());
    }

}