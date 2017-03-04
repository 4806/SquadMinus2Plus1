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
public class FullWikiPageResultTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private UserRepository userRepository;

    private WikiPage testWikiPage1;
    private WikiPage testWikiPage2;

    private FullWikiPageResult testResult1;
    private FullWikiPageResult testResult2;

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

        testResult1 = new FullWikiPageResult(testWikiPage1.getId(), testWikiPage1.getTitle(), testWikiPage1.getParentID(),testWikiPage1.getAuthor().getUserName(),testWikiPage1.getCreationDate(),testWikiPage1.getContent());
        testResult2 = new FullWikiPageResult(testWikiPage2.getId(), testWikiPage2.getTitle(), testWikiPage2.getParentID(),testWikiPage2.getAuthor().getUserName(),testWikiPage2.getCreationDate(),testWikiPage2.getContent());

    }

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Failure - getContent() for testResult1", testWikiPage1.getContent(), testResult1.getContent());
        assertEquals("Failure - getContent() for testResult2", testWikiPage2.getContent(), testResult2.getContent());
    }

    @Test
    public void getFullResult() throws Exception {
        assertEquals("Failure - getFullResult(testWikiPage1)", testResult1, FullWikiPageResult.getFullResult(testWikiPage1));
        assertEquals("Failure - getFullResult(testWikiPage2)", testResult2, FullWikiPageResult.getFullResult(testWikiPage2));
    }

}