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

import static org.junit.Assert.assertEquals;

/**
 * Created by Chris on 3/4/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class WikiPageResultTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private UserRepository userRepository;

    private WikiPage testWikiPage1;
    private WikiPage testWikiPage2;

    private WikiPageResult testResult1;
    private WikiPageResult testResult2;

    private User testUser1;
    private User testUser2;

    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

        testWikiPage1 = new WikiPage("testTitle1", "testContent1", testUser1);
        testWikiPage1 = wikiPageRepository.save(testWikiPage1);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(), testUser2);
        testWikiPage2 = wikiPageRepository.save(testWikiPage2);

        testResult1 = new WikiPageResult(testWikiPage1.getId(), testWikiPage1.getTitle(), testWikiPage1.getParentID(),testWikiPage1.getAuthor().getUserName(),testWikiPage1.getCreationDate());
        testResult2 = new WikiPageResult(testWikiPage2.getId(), testWikiPage2.getTitle(), testWikiPage2.getParentID(),testWikiPage2.getAuthor().getUserName(),testWikiPage2.getCreationDate());

    }

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getResult() throws Exception {
        assertEquals("Failure - getResult(testWikiPage1)", testResult1, WikiPageResult.getResult(testWikiPage1));
        assertEquals("Failure - getResult(testWikiPage2)", testResult2, WikiPageResult.getResult(testWikiPage2));
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Failure - getId() for testResult1", testWikiPage1.getId(), testResult1.getId());
        assertEquals("Failure - getId() for testResult2", testWikiPage2.getId(), testResult2.getId());
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals("Failure - getTitle() for testResult1", testWikiPage1.getTitle(), testResult1.getTitle());
        assertEquals("Failure - getTitle() for testResult2", testWikiPage2.getTitle(), testResult2.getTitle());
    }

    @Test
    public void getParentID() throws Exception {
        assertEquals("Failure - getParentID() for testResult1", testWikiPage1.getParentID(), testResult1.getParentID());
        assertEquals("Failure - getParentID() for testResult2", testWikiPage2.getParentID(), testResult2.getParentID());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals("Failure - getAuthor() for testResult1", testWikiPage1.getAuthor().getUserName(), testResult1.getAuthor());
        assertEquals("Failure - getAuthor() for testResult2", testWikiPage2.getAuthor().getUserName(), testResult2.getAuthor());
    }

    @Test
    public void getCreationDate() throws Exception {
        assertEquals("Failure - getCreationDate() for testResult1", testWikiPage1.getCreationDate(), testResult1.getCreationDate());
        assertEquals("Failure - getCreationDate() for testResult2", testWikiPage2.getCreationDate(), testResult2.getCreationDate());
    }

}