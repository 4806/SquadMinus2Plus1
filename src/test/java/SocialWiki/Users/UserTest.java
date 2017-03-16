package SocialWiki.Users;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by connor on 2/23/17.
 */
public class UserTest {

    private User user1;

    @Before
    public void setUp() throws Exception {
        user1 = new User(1L, "testUserName1", "testFirstName1", "testLastName1", "testEmail1");
        user1.setPassword("testPassword1");
    }

    @Test
    public void asSessionUser() throws Exception {
        User sessionUser = user1.asSessionUser();
        assertEquals("Failure - id for asSessionUser()", 1L, sessionUser.getId());
        assertEquals("Failure - userName for asSessionUser()", "testUserName1", sessionUser.getUserName());
        assertEquals("Failure - firstName for asSessionUser()", "testFirstName1", sessionUser.getFirstName());
        assertEquals("Failure - lastName for asSessionUser()", "testLastName1", sessionUser.getLastName());
        assertEquals("Failure - email for asSessionUser()", "testEmail1", sessionUser.getEmail());
        assertEquals("Failure - password for asSessionUser()", null, sessionUser.getPassword());
    }

    @Test
    public void equals() throws Exception {
        User testUser = new User(1L, "testUserName1", "testFirstName1", "testLastName1", "testEmail1");
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
        assertTrue("Failure - deleted user is not flagged as deleted", user1.isDeleted());
    }
}