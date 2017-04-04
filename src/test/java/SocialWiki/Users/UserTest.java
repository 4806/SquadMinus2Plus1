package SocialWiki.Users;

import SocialWiki.WikiPages.ConcreteWikiPage;
import SocialWiki.WikiPages.WikiPageWithAuthorProxy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by connor on 2/23/17.
 */
public class UserTest {

    private User user1;
    private User user2;
    private ConcreteWikiPage page1;

    @Before
    public void setUp() throws Exception {
        user1 = new User(1L, "testUserName1", "testFirstName1", "testLastName1", "Test1@email.com", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
        user2 = new User(2L, "testUserName2", "testFirstName2", "testLastName2", "Test2@email.com", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
        user1.setPassword("testPassword1");
        user2.setPassword("testPassword2");
        page1 = new ConcreteWikiPage("testTitle1", "testContent1", user1);
    }

    @Test
    public void asSessionUser() throws Exception {
        User sessionUser = user1.asSessionUser();
        assertEquals("Failure - id for asSessionUser()", 1L, sessionUser.getId());
        assertEquals("Failure - userName for asSessionUser()", "testUserName1", sessionUser.getUserName());
        assertEquals("Failure - firstName for asSessionUser()", "testFirstName1", sessionUser.getFirstName());
        assertEquals("Failure - lastName for asSessionUser()", "testLastName1", sessionUser.getLastName());
        assertEquals("Failure - email for asSessionUser()", "Test1@email.com", sessionUser.getEmail());
        assertEquals("Failure - password for asSessionUser()", null, sessionUser.getPassword());
        assertFalse("Failure - isDeleted for asSessionUser()", sessionUser.isDeleted());
        assertEquals("Failure - likedPages for asSessionUser()", new ArrayList<ConcreteWikiPage>(), sessionUser.getLikedPages());
        assertEquals("Failure - createdPages for asSessionUSer()", new ArrayList<ConcreteWikiPage>(), sessionUser.getCreatedPages());
    }

    @Test
    public void equals() throws Exception {
        User testUser = new User(1L, "testUserName1", "testFirstName1", "testLastName1", "Test1@email.com", false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
        assertTrue("Failure - User.equals()", testUser.equals(user1));
    }

    @Test
    public void delete() throws Exception {
        user1.delete();
        assertEquals("Failure - id for deleted user not persisted", 1L, user1.getId());
        assertEquals("Failure - userName for deleted user not persisted", "testUserName1", user1.getUserName());
        assertNull("Failure - firstName for deleted user is not null", user1.getFirstName());
        assertNull("Failure - lastName for deleted user is not null", user1.getLastName());
        assertNull("Failure - email for deleted user is not null", user1.getEmail());
        assertNull("Failure - password for deleted user is not null", user1.getPassword());
        assertNull("Failure - likedPages for deleted user is not null", user1.getLikedPages());
        assertTrue("Failure - deleted user is not flagged as deleted", user1.isDeleted());
    }

    @Test
    public void likePage() throws Exception {
        user1.likePage(page1);
        assertTrue("Failure - the newly liked page is not in the user's liked pages", user1.getLikedPages().contains(page1));
    }

    @Test
    public void unlikePage() throws Exception {
        user1.likePage(page1);
        user1.unlikePage(page1);
        assertFalse("Failure - the newly unliked page is in the user's liked pages", user1.getLikedPages().contains(page1));
    }

    @Test
    public void followUser() throws Exception {
        user1.followUser(user2);
        assertTrue("Failure - the newly followed user is not in the user's followed users", user1.getFollowedUsers().contains(user2));

        user1.followUser(user1);
        assertTrue("Failure - User following itself is not in the user's followed users", user1.getFollowedUsers().contains(user1));
    }

    @Test
    public void unfollowUser() throws Exception {
        user1.followUser(user2);
        user1.unfollowUser(user2);
        assertFalse("Failure -the newly unfollowed user is in the user's followed users", user1.getFollowedUsers().contains(user2));
    }

    @Test
    public void addCreatedPage() throws Exception {
        user1.addCreatedPage(page1);
        assertTrue("Failure - the newly created page is not in the user's created pages", user1.getCreatedPages().contains(page1));
    }

    @Test
    public void getLikedProxyPages() throws Exception {
        user1.likePage(page1);
        assertTrue("Failure - the newly liked page is not in the user's proxy liked page list", user1.getLikedProxyPages().contains(new WikiPageWithAuthorProxy(page1)));
    }

    @Test
    public void getCreatedProxyPages() throws Exception {
        user1.addCreatedPage(page1);
        assertTrue("Failure - the newly created page is not in the user's proxy created page list", user1.getCreatedProxyPages().contains(new WikiPageWithAuthorProxy(page1)));
    }

    @Test
    public void addNotification() throws Exception {
        user1.addNotification("test notification");
        assertTrue("Failure - the newly made notification is not in the user's notification list", user1.getNotifications().contains("test notification"));
    }

    @Test
    public void removeNotification() throws Exception {
        user1.addNotification("test notification");
        user1.removeNotification("test notification");
        assertTrue("Failure - the removed notification is in the user's notification list", !user1.getNotifications().contains("test notification"));
    }
}