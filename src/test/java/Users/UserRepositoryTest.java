package Users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by connor on 2/24/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        user2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        userRepo.save(user1);
        userRepo.save(user2);
    }

    @After
    public void tearDown() throws Exception {
        userRepo.deleteAll();
    }

    @Test
    public void findByUserName() throws Exception {
        List<User> users = userRepo.findByUserName("testUserName1");
        assertEquals("Failure - userRepository does not have a unique userName for user1", 1, users.size());
        assertEquals("Failure - userRepository query by userName does not return user1", user1, users.get(0));

        users = userRepo.findByUserName("testUserName2");
        assertEquals("Failure - userRepository does not have a unique userName for user2", 1, users.size());
        assertEquals("Failure - userRepository query by userName does not return user2", user2, users.get(0));

        users = userRepo.findByUserName("testUsername3");
        assertEquals("Failure - userRepository found a user with a userName that does not exist", 0, users.size());
    }

    @Test
    public void findByEmail() throws Exception {
        List<User> users = userRepo.findByEmail("testEmail1");
        assertEquals("Failure - userRepository does not have a unique email for user1", 1, users.size());
        assertEquals("Failure - userRepository query by email does not return user1", user1, users.get(0));

        users = userRepo.findByEmail("testEmail2");
        assertEquals("Failure - userRepository does not have a unique email for user2", 1, users.size());
        assertEquals("Failure - userRepository query by email does not return user2", user2, users.get(0));

        users = userRepo.findByEmail("testEmail3");
        assertEquals("Failure - userRepository found a user with an email that does not exist", 0, users.size());
    }

    @Test
    public void findByUserNameAndPassword() throws Exception {
        List<User> users = userRepo.findByUserNameAndPassword("testUserName1", "testPassword1");
        assertEquals("Failure - userRepository does not have a unique userName-password pair for user1", 1, users.size());
        assertEquals("Failure - userRepository query by userName and password does not return user1", user1, users.get(0));

        users = userRepo.findByUserNameAndPassword("testUserName2", "testPassword2");
        assertEquals("Failure - userRepository does not have a unique userName-password pair for user2", 1, users.size());
        assertEquals("Failure - userRepository query by userName and password does not return user2", user2, users.get(0));

        users = userRepo.findByUserNameAndPassword("testUsername1", "testPassword2");
        assertEquals("Failure - userRepository found a user with a userName-password pair that does not exist", 0, users.size());
    }

    @Test
    public void findByEmailAndPassword() throws Exception {
        List<User> users = userRepo.findByEmailAndPassword("testEmail1", "testPassword1");
        assertEquals("Failure - userRepository does not have a unique email-password pair for user1", 1, users.size());
        assertEquals("Failure - userRepository query by email and password does not return user1", user1, users.get(0));

        users = userRepo.findByEmailAndPassword("testEmail2", "testPassword2");
        assertEquals("Failure - userRepository does not have a unique email-password pair for user2", 1, users.size());
        assertEquals("Failure - userRepository query by email and password does not return user2", user2, users.get(0));

        users = userRepo.findByEmailAndPassword("testEmail1", "testPassword2");
        assertEquals("Failure - userRepository found a user with a email-password pair that does not exist", 0, users.size());
    }

}