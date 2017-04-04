package SocialWiki.Notifications;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Chris on 3/30/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserNotificationsAspectTest {

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


    @Before
    public void setUp() throws Exception {
        user1 = userRepo.save(new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1"));
        user2 = userRepo.save(new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2"));
        user3 = userRepo.save(new User("testUserName3", "testFirstName3", "testLastName3", "testEmail3", "testPassword3"));
        page1 = pageRepo.save(new ConcreteWikiPage("testTitle1", "testContent1", user1));

        user2.followUser(user1);
        user3.followUser(user1);
        user2 = userRepo.save(user2);
        user3 = userRepo.save(user3);
    }

    @After
    public void tearDown() throws Exception {
        pageRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    public void userNotificationAdvice() throws Exception {

        //************************** Test Advice after Page creation
        // perform login to get session
        MvcResult result = mockMvc.perform(post("/login")
                .content("login=" + user1.getUserName() + "&pass=" + user1.getPassword())
                .contentType("application/x-www-form-urlencoded"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        MultiValueMap<String, String> params = new HttpHeaders();

        //create wiki page
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", "-1");
        result = this.mockMvc.perform(post("/createWikiPage")
                .params(params)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("testTitle")))
                .andExpect(jsonPath("$.content", is("testContent")))
                .andExpect(jsonPath("$.parentID", is(-1)))
                .andReturn();
        params.clear();

        user2 = userRepo.findOne(user2.getId());
        user3 = userRepo.findOne(user3.getId());

        //Check that user2 has received notification
        assertEquals("Failure - user2 did not receive notification from page creation", 1, user2.getNotifications().size());
        assertTrue("Failure - user2 did not receive correctly formatted notification from page creation",
                user2.getNotifications().get(0).contains("The user " + user1.getUserName() + " has just created the page testTitle"));

        //Check that user3 has received notification
        assertEquals("Failure - user3 did not receive notification from page creation", 1, user3.getNotifications().size());
        assertTrue("Failure - user3 did not receive correctly formatted notification from page creation",
                user3.getNotifications().get(0).contains("The user " + user1.getUserName() + " has just created the page testTitle"));

        //************************** Test Advice after Page edit

        //edit wiki page
        params.add("title", "testTitle");
        params.add("content", "testContent");
        params.add("parentID", page1.getId().toString());
        result = this.mockMvc.perform(post("/createWikiPage")
                .params(params)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("testTitle")))
                .andExpect(jsonPath("$.content", is("testContent")))
                .andExpect(jsonPath("$.parentID", is(Integer.parseInt(page1.getId().toString()))))
                .andReturn();
        params.clear();

        user2 = userRepo.findOne(user2.getId());
        user3 = userRepo.findOne(user3.getId());

        //Check that user2 has received notification
        assertEquals("Failure - user2 did not receive notification from page editing", 2, user2.getNotifications().size());
        assertTrue("Failure - user2 did not receive correctly formatted notification from page editing",
                user2.getNotifications().get(1).contains("The user " + user1.getUserName() + " has just edited the page testTitle"));

        //Check that user3 has received notification
        assertEquals("Failure - user3 did not receive notification from page editing", 2, user3.getNotifications().size());
        assertTrue("Failure - user3 did not receive correctly formatted notification from page editing",
                user3.getNotifications().get(1).contains("The user " + user1.getUserName() + " has just edited the page testTitle"));

        //************************** Test Advice after Page liked

        // Like page
        mockMvc.perform(post("/likePage")
                .content("id=" + page1.getId())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        user2 = userRepo.findOne(user2.getId());
        user3 = userRepo.findOne(user3.getId());

        //Check that user2 has received notification
        assertEquals("Failure - user2 did not receive notification from page like", 3, user2.getNotifications().size());
        assertTrue("Failure - user2 did not receive correctly formatted notification from page like",
                user2.getNotifications().get(2).contains("The user " + user1.getUserName() + " has just liked the page testTitle1"));

        //Check that user3 has received notification
        assertEquals("Failure - user3 did not receive notification from page like", 3, user3.getNotifications().size());
        assertTrue("Failure - user3 did not receive correctly formatted notification from page like",
                user3.getNotifications().get(2).contains("The user " + user1.getUserName() + " has just liked the page testTitle1"));

        //************************** Test Advice after User followed

        // Follow user
        mockMvc.perform(post("/followUser")
                .content("user=" + user2.getUserName())
                .contentType("application/x-www-form-urlencoded")
                .session(session))
                .andExpect(status().isNoContent());

        user2 = userRepo.findOne(user2.getId());
        user3 = userRepo.findOne(user3.getId());

        //Check that user2 has received notification
        assertEquals("Failure - user2 did not receive notification from user follow", 4, user2.getNotifications().size());
        assertTrue("Failure - user2 did not receive correctly formatted notification from user follow",
                user2.getNotifications().get(3).contains("The user " + user1.getUserName() + " has just followed " +  user2.getUserName()));

        //Check that user3 has received notification
        assertEquals("Failure - user3 did not receive notification from user follow", 4, user3.getNotifications().size());
        assertTrue("Failure - user3 did not receive correctly formatted notification from user follow",
                user3.getNotifications().get(3).contains("The user " + user1.getUserName() + " has just followed " +  user2.getUserName()));

    }

}