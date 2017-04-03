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
 * Unit test for WikiPageWithAuthorAndContentProxy class
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class WikiPageWithAuthorAndContentProxyTest {

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private UserRepository userRepository;

    private ConcreteWikiPage testConcreteWikiPage1;
    private ConcreteWikiPage testConcreteWikiPage2;

    private WikiPageWithAuthorAndContentProxy testResult1;
    private WikiPageWithAuthorAndContentProxy testResult2;

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

        testResult1 = new WikiPageWithAuthorAndContentProxy(testConcreteWikiPage1);
        testResult2 = new WikiPageWithAuthorAndContentProxy(testConcreteWikiPage2);

    }

    @After
    public void tearDown() throws Exception {
        wikiPageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Failure - getContent() for testResult1", testConcreteWikiPage1.getContent(), testResult1.getContent());
        assertEquals("Failure - getContent() for testResult2", testConcreteWikiPage2.getContent(), testResult2.getContent());
    }

    @Test
    public void getFullResult() throws Exception {
        assertEquals("Failure - getFullResult(testConcreteWikiPage1)", testResult1, WikiPageWithAuthorAndContentProxy.getFullResult(testConcreteWikiPage1));
        assertEquals("Failure - getFullResult(testConcreteWikiPage2)", testResult2, WikiPageWithAuthorAndContentProxy.getFullResult(testConcreteWikiPage2));
    }

    @Test
    public void isAuthorDeleted() throws Exception {
        assertEquals("Failure - isAuthorDeleted() for testResult1", testUser1.isDeleted(), testResult1.isAuthorDeleted());
        assertEquals("Failure - isAuthorDeleted() for testResult1", testUser2.isDeleted(), testResult2.isAuthorDeleted());
    }
}