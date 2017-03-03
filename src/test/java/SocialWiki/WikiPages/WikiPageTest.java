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
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WikiPageTest {

    private WikiPage testWikiPage1;
    private WikiPage testWikiPage2;

    private User testUser1;
    private User testUser2;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getTitle() throws Exception {
        assertEquals("Failure - getTitle for testWikiPage1", "testTitle1", testWikiPage1.getTitle());
        assertEquals("Failure - getTitle for testWikiPage2", "testTitle2", testWikiPage2.getTitle());
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Failure - getContent for testWikiPage1", "testContent1", testWikiPage1.getContent());
        assertEquals("Failure - getContent for testWikiPage2", "testContent2", testWikiPage2.getContent());
    }

    @Test
    public void getParentID() throws Exception {
        assertEquals("Failure - getParentID for testWikiPage1", WikiPage.IS_ORIGINAL_ID, testWikiPage1.getParentID());
        assertEquals("Failure - getParentID for testWikiPage2", testWikiPage1.getId(), testWikiPage2.getParentID());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals("Failure - getAuthor for testWikiPage1", testUser1, testWikiPage1.getAuthor());
        assertEquals("Failure - getAuthor for testWikiPage2", testUser2, testWikiPage2.getAuthor());
    }

    @Test
    public void getCreationDate() throws Exception {

        Calendar date1 = testWikiPage1.getCreationDate();
        Calendar date2 = testWikiPage2.getCreationDate();
        Calendar now = Calendar.getInstance();

        //Only testing up to current day, possibility of failing if get any more specific
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        assertEquals("Failure - getCreationDate for testWikiPage1", dateFormat.format(now.getTime()), dateFormat.format(date1.getTime()));
        assertEquals("Failure - getCreationDate for testWikiPage2", dateFormat.format(now.getTime()), dateFormat.format(date2.getTime()));
    }

    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

        testWikiPage1 = new WikiPage("testTitle1", "testContent1", testUser1);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(), testUser2);

    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }
}