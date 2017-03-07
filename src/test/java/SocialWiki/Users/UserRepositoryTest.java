package SocialWiki.Users;

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
        User user = userRepo.findByUserName("testUserName1");
        assertEquals("Failure - userRepository query by userName does not return user1", user1, user);

        user = userRepo.findByUserName("testUserName2");
        assertEquals("Failure - userRepository query by userName does not return user2", user2, user);

        user = userRepo.findByUserName("testUserName3");
        assertEquals("Failure - userRepository found a user with a userName that does not exist", null, user);

        user = userRepo.findByUserName("");
        assertEquals("Failure - userRepository found a user with a blank userName", null, user);

        user = userRepo.findByUserName(null);
        assertEquals("Failure - userRepository found a user with a null userName", null, user);
    }

    @Test
    public void findByEmail() throws Exception {
        User user = userRepo.findByEmail("testEmail1");
        assertEquals("Failure - userRepository query by email does not return user1", user1, user);

        user = userRepo.findByEmail("testEmail2");
        assertEquals("Failure - userRepository query by email does not return user2", user2, user);

        user = userRepo.findByEmail("testEmail3");
        assertEquals("Failure - userRepository found a user with an email that does not exist", null, user);

        user = userRepo.findByEmail("");
        assertEquals("Failure - userRepository found a user with a blank email", null, user);

        user = userRepo.findByEmail(null);
        assertEquals("Failure - userRepository found a user with a null email", null, user);
    }

    @Test
    public void findByUserNameAndPassword() throws Exception {
        User user = userRepo.findByUserNameAndPassword("testUserName1", "testPassword1");
        assertEquals("Failure - userRepository query by userName and password does not return user1", user1, user);

        user = userRepo.findByUserNameAndPassword("testUserName2", "testPassword2");
        assertEquals("Failure - userRepository query by userName and password does not return user2", user2, user);

        user = userRepo.findByUserNameAndPassword("testUserName1", "testPassword2");
        assertEquals("Failure - userRepository found a user with a userName-password pair that does not exist", null, user);

        user = userRepo.findByUserNameAndPassword("", "testPassword2");
        assertEquals("Failure - userRepository found a user with a blank userName", null, user);

        user = userRepo.findByUserNameAndPassword("testUserName2", "");
        assertEquals("Failure - userRepository found a user with a blank password", null, user);

        user = userRepo.findByUserNameAndPassword("", "");
        assertEquals("Failure - userRepository found a user with a blank userName and password", null, user);

        user = userRepo.findByUserNameAndPassword(null, "testPassword2");
        assertEquals("Failure - userRepository found a user with a null userName", null, user);

        user = userRepo.findByUserNameAndPassword("testUserName2", null);
        assertEquals("Failure - userRepository found a user with a null password", null, user);

        user = userRepo.findByUserNameAndPassword(null, null);
        assertEquals("Failure - userRepository found a user with a null userName and password", null, user);
    }

    @Test
    public void findByEmailAndPassword() throws Exception {
        User user = userRepo.findByEmailAndPassword("testEmail1", "testPassword1");
        assertEquals("Failure - userRepository query by email and password does not return user1", user1, user);

        user = userRepo.findByEmailAndPassword("testEmail2", "testPassword2");
        assertEquals("Failure - userRepository query by email and password does not return user2", user2, user);

        user = userRepo.findByEmailAndPassword("testEmail1", "testPassword2");
        assertEquals("Failure - userRepository found a user with a email-password pair that does not exist", null, user);

        user = userRepo.findByEmailAndPassword("", "testPassword2");
        assertEquals("Failure - userRepository found a user with a blank email", null, user);

        user = userRepo.findByEmailAndPassword("testEmail2", "");
        assertEquals("Failure - userRepository found a user with a blank password", null, user);

        user = userRepo.findByEmailAndPassword("", "");
        assertEquals("Failure - userRepository found a user with a blank email and password", null, user);

        user = userRepo.findByEmailAndPassword(null, "testPassword2");
        assertEquals("Failure - userRepository found a user with a null email", null, user);

        user = userRepo.findByEmailAndPassword("testEmail2", null);
        assertEquals("Failure - userRepository found a user with a null password", null, user);

        user = userRepo.findByEmailAndPassword(null, null);
        assertEquals("Failure - userRepository found a user with a null email and password", null, user);
    }

    @Test
    public void findByUserNameOrEmail() throws Exception {
        List<User> users = userRepo.findByUserNameOrEmail("testUserName1", "testEmail1");
        assertEquals("Failure - userRepository query with existing userName and email for user1 could not be found", user1, users.get(0));

        users = userRepo.findByUserNameOrEmail("testUserName1", "testEmail3");
        assertEquals("Failure - userRepository query with existing userName for user1 could not be found", user1, users.get(0));

        users = userRepo.findByUserNameOrEmail("testUserName3", "testEmail1");
        assertEquals("Failure - userRepository query with existing email for user1 could not be found", user1, users.get(0));

        users = userRepo.findByUserNameOrEmail("testUserName1", "testEmail2");
        assertEquals("Failure - userRepository query with existing userName for user1 and email for user2 could not find both", 2, users.size());
        assertEquals("Failure - userRepository query with existing userName for user1 could not be found", user1, users.get(0));
        assertEquals("Failure - userRepository query with existing email for user2 could not be found", user2, users.get(1));

        users = userRepo.findByUserNameOrEmail("testUserName3", "testEmail3");
        assertEquals("Failure - userRepository query by userName or email returned users that do not exist", 0, users.size());

        users = userRepo.findByUserNameOrEmail("", "");
        assertEquals("Failure - userRepository query by userName or email returned users with blank userName or email", 0, users.size());

        users = userRepo.findByUserNameOrEmail(null, null);
        assertEquals("Failure - userRepository query by userName or email returned users with null userName or email", 0, users.size());
    }

}