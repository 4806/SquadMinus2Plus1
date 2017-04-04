package SocialWiki.WikiPages;

import SocialWiki.Users.User;
import SocialWiki.Users.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris on 2/24/2017.
 * Unit tests for ConcreteWikiPage and WikiPage classes
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcreteWikiPageTest {

    private ConcreteWikiPage testConcreteWikiPage1;
    private ConcreteWikiPage testConcreteWikiPage2;

    private User testUser1;
    private User testUser2;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getTitle() throws Exception {
        assertEquals("Failure - getTitle for testConcreteWikiPage1", "testTitle1", testConcreteWikiPage1.getTitle());
        assertEquals("Failure - getTitle for testConcreteWikiPage2", "testTitle2", testConcreteWikiPage2.getTitle());
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Failure - getContent for testConcreteWikiPage1", "testContent1", testConcreteWikiPage1.getContent());
        assertEquals("Failure - getContent for testConcreteWikiPage2", "testContent2", testConcreteWikiPage2.getContent());
    }

    @Test
    public void getParentID() throws Exception {
        assertEquals("Failure - getParentID for testConcreteWikiPage1", ConcreteWikiPage.IS_ORIGINAL_ID, testConcreteWikiPage1.getParentID());
        assertEquals("Failure - getParentID for testConcreteWikiPage2", testConcreteWikiPage1.getId(), testConcreteWikiPage2.getParentID());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals("Failure - getAuthor for testConcreteWikiPage1", testUser1, testConcreteWikiPage1.getAuthor());
        assertEquals("Failure - getAuthor for testConcreteWikiPage2", testUser2, testConcreteWikiPage2.getAuthor());
    }

    @Test
    public void getCreationDate() throws Exception {

        Calendar date1 = testConcreteWikiPage1.getCreationDate();
        Calendar date2 = testConcreteWikiPage2.getCreationDate();
        Calendar now = Calendar.getInstance();

        //Only testing up to current day, possibility of failing if get any more specific
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        assertEquals("Failure - getCreationDate for testConcreteWikiPage1", dateFormat.format(now.getTime()), dateFormat.format(date1.getTime()));
        assertEquals("Failure - getCreationDate for testConcreteWikiPage2", dateFormat.format(now.getTime()), dateFormat.format(date2.getTime()));
    }

    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "Test1@email.com", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "Test2@email.com", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

        testConcreteWikiPage1 = new ConcreteWikiPage("testTitle1", "testContent1", testUser1);
        testConcreteWikiPage2 = new ConcreteWikiPage("testTitle2", "testContent2", testConcreteWikiPage1.getId(), testUser2);

    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }
}