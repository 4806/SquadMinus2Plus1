package Users;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by connor on 2/23/17.
 */
public class UserTest {

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User();
        user2 = new User("testUserName", "testFirstName", "testLastName", "testEmail", "testPassword");
    }

    @Test
    public void getUserName() throws Exception {
        assertEquals("failure - getUserName", "testUserName", user2.getUserName());
    }

    @Test
    public void setUserName() throws Exception {
        user1.setUserName("testUserName1");
        user2.setUserName("testUserName2");
        assertEquals("failure - setUserName for user1", "testUserName1", user1.getUserName());
        assertEquals("failure - setUserName for user2", "testUserName2", user2.getUserName());
    }

    @Test
    public void getFirstName() throws Exception {
        assertEquals("failure - getFirstName", "testFirstName", user2.getFirstName());
    }

    @Test
    public void setFirstName() throws Exception {
        user1.setFirstName("testFirstName1");
        user2.setFirstName("testFirstName2");
        assertEquals("failure - setFirstName for user1", "testFirstName1", user1.getFirstName());
        assertEquals("failure - setFirstName for user2", "testFirstName2", user2.getFirstName());
    }

    @Test
    public void getLastName() throws Exception {
        assertEquals("failure - getLastName", "testLastName", user2.getLastName());
    }

    @Test
    public void setLastName() throws Exception {
        user1.setLastName("testLastName1");
        user2.setLastName("testLastName2");
        assertEquals("failure - setLastName for user1", "testLastName1", user1.getLastName());
        assertEquals("failure - setLastName for user2", "testLastName2", user2.getLastName());
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals("failure - getEmail", "testEmail", user2.getEmail());
    }

    @Test
    public void setEmail() throws Exception {
        user1.setEmail("testEmail1");
        user2.setEmail("testEmail2");
        assertEquals("failure - setEmail for user1", "testEmail1", user1.getEmail());
        assertEquals("failure - setEmail for user2", "testEmail2", user2.getEmail());
    }

    @Test
    public void getPassword() throws Exception {
        assertEquals("failure - getPassword", "testPassword", user2.getPassword());
    }

    @Test
    public void setPassword() throws Exception {
        user1.setPassword("testPassword1");
        user2.setPassword("testPassword2");
        assertEquals("failure - setPassword for user1", "testPassword1", user1.getPassword());
        assertEquals("failure - setPassword for user2", "testPassword2", user2.getPassword());
    }

}