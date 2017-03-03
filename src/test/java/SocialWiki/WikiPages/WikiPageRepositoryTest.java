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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Chris on 2/25/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WikiPageRepositoryTest {

    @Autowired
    private WikiPageRepository repo;

    @Autowired
    private UserRepository userRepository;

    WikiPage testWikiPage1;
    WikiPage testWikiPage2;
    WikiPage testWikiPage3;
    WikiPage testWikiPage4;

    User testUser1;
    User testUser2;
    User testUser3;

    @Test
    public void findByTitle() throws Exception {
        List<WikiPage> pages = repo.findByTitle("testTitle1");

        assertEquals("Failure - Number of pages found by findByTable('testTitle1') should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByTable('testTitle1') is not correct", testWikiPage1.getId(), pages.get(0).getId());
        pages.clear();

        pages = repo.findByTitle("testTitle2");

        assertEquals("Failure - Number of pages found by findByTable('testTitle2') should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByTable('testTitle2') is not correct", testWikiPage2.getId(), pages.get(0).getId());

        List<Long> pageIDs = new ArrayList<>();
        pageIDs.add(testWikiPage3.getId());
        pageIDs.add(testWikiPage4.getId());

        pages = repo.findByTitle("testTitlePair");

        assertEquals("Failure - Number of pages found by findByTable('testTitlePair') should be 2", 2, pages.size());
        assertTrue("Failure - First page found by findByTable('testTitlePair') is not correct", pageIDs.contains(pages.get(0).getId()));
        assertTrue("Failure - Second page found by findByTable('testTitlePair') is not correct", pageIDs.contains(pages.get(1).getId()));

    }

    @Test
    public void findByAuthorID() throws Exception {
        List<WikiPage> pages = repo.findByAuthorID(testUser1.getId());

        assertEquals("Failure - Number of pages found by findByAuthorID(testUser1.getId()) should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByAuthorID(testUser1.getId()) is not correct", testWikiPage1.getId(), pages.get(0).getId());
        pages.clear();

        pages = repo.findByAuthorID(testUser2.getId());;

        assertEquals("Failure - Number of pages found by findByAuthorID(testUser2.getId()) should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByAuthorID(testUser2.getId()) is not correct", testWikiPage2.getId(), pages.get(0).getId());

        List<Long> pageIDs = new ArrayList<>();
        pageIDs.add(testWikiPage3.getId());
        pageIDs.add(testWikiPage4.getId());

        pages = repo.findByAuthorID(testUser3.getId());;

        assertEquals("Failure - Number of pages found by findByAuthorID(testUser3.getId()) should be 2", 2, pages.size());
        assertTrue("Failure - First page found by findByAuthorID(testUser3.getId()) is not correct", pageIDs.contains(pages.get(0).getId()));
        assertTrue("Failure - Second page found by findByAuthorID(testUser3.getId()) is not correct", pageIDs.contains(pages.get(1).getId()));

    }


    @Before
    public void setUp() throws Exception {
        testUser1 = new User("testUserName1", "testFirstName1", "testLastName1", "testEmail1", "testPassword1");
        testUser2 = new User("testUserName2", "testFirstName2", "testLastName2", "testEmail2", "testPassword2");
        testUser3 = new User("testUserName3", "testFirstName3", "testLastName3", "testEmail3", "testPassword3");
        testUser1 = userRepository.save(testUser1);
        testUser2 = userRepository.save(testUser2);
        testUser3 = userRepository.save(testUser3);

        testWikiPage1 = new WikiPage("testTitle1", "testContent1",testUser1.getId(),testUser1);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(),testUser2.getId(),testUser2);
        testWikiPage3 = new WikiPage("testTitlePair", "testContent1",testUser3.getId(),testUser3);
        testWikiPage4 = new WikiPage("testTitlePair", "testContent2", testWikiPage3.getId(),testUser3.getId(),testUser3);

        testWikiPage1 = repo.save(testWikiPage1);
        testWikiPage2 = repo.save(testWikiPage2);
        testWikiPage3 = repo.save(testWikiPage3);
        testWikiPage4 = repo.save(testWikiPage4);

    }

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
        userRepository.deleteAll();
    }
}