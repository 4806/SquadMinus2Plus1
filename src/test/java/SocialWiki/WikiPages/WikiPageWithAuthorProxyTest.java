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
 * Unit test for WikiPageWithAuthorProxy class
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class WikiPageWithAuthorProxyTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private UserRepository userRepository;

    private ConcreteWikiPage testConcreteWikiPage1;
    private ConcreteWikiPage testConcreteWikiPage2;

    private WikiPageWithAuthorProxy testResult1;
    private WikiPageWithAuthorProxy testResult2;

    private User testUser1;
    private User testUser2;

    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);

        testConcreteWikiPage1 = new ConcreteWikiPage("testTitle1", "testContent1", testUser1);
        testConcreteWikiPage1 = wikiPageRepository.save(testConcreteWikiPage1);
        testConcreteWikiPage2 = new ConcreteWikiPage("testTitle2", "testContent2", testConcreteWikiPage1.getId(), testUser2);
        testConcreteWikiPage2 = wikiPageRepository.save(testConcreteWikiPage2);

        testResult1 = new WikiPageWithAuthorProxy(testConcreteWikiPage1);
        testResult2 = new WikiPageWithAuthorProxy(testConcreteWikiPage2);

    }

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getResult() throws Exception {
        assertEquals("Failure - getResult(testConcreteWikiPage1)", testResult1, WikiPageWithAuthorProxy.getResult(testConcreteWikiPage1));
        assertEquals("Failure - getResult(testConcreteWikiPage2)", testResult2, WikiPageWithAuthorProxy.getResult(testConcreteWikiPage2));
    }

    @Test
    public void getId() throws Exception {
        assertEquals("Failure - getId() for testResult1", testConcreteWikiPage1.getId(), testResult1.getId());
        assertEquals("Failure - getId() for testResult2", testConcreteWikiPage2.getId(), testResult2.getId());
    }

    @Test
    public void getTitle() throws Exception {
        assertEquals("Failure - getTitle() for testResult1", testConcreteWikiPage1.getTitle(), testResult1.getTitle());
        assertEquals("Failure - getTitle() for testResult2", testConcreteWikiPage2.getTitle(), testResult2.getTitle());
    }

    @Test
    public void getParentID() throws Exception {
        assertEquals("Failure - getParentID() for testResult1", testConcreteWikiPage1.getParentID(), testResult1.getParentID());
        assertEquals("Failure - getParentID() for testResult2", testConcreteWikiPage2.getParentID(), testResult2.getParentID());
    }

    @Test
    public void getAuthor() throws Exception {
        assertEquals("Failure - getAuthor() for testResult1", testConcreteWikiPage1.getAuthor().getUserName(), testResult1.getAuthor());
        assertEquals("Failure - getAuthor() for testResult2", testConcreteWikiPage2.getAuthor().getUserName(), testResult2.getAuthor());
    }

    @Test
    public void getCreationDate() throws Exception {
        assertEquals("Failure - getCreationDate() for testResult1", testConcreteWikiPage1.getCreationDate(), testResult1.getCreationDate());
        assertEquals("Failure - getCreationDate() for testResult2", testConcreteWikiPage2.getCreationDate(), testResult2.getCreationDate());
    }

}