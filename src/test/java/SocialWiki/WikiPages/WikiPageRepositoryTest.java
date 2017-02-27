package SocialWiki.WikiPages;

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

    WikiPage testWikiPage1;
    WikiPage testWikiPage2;
    WikiPage testWikiPage3;
    WikiPage testWikiPage4;

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
        List<WikiPage> pages = repo.findByAuthorID(1L);

        assertEquals("Failure - Number of pages found by findByAuthorID(1L) should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByAuthorID(1L) is not correct", testWikiPage1.getId(), pages.get(0).getId());
        pages.clear();

        pages = repo.findByAuthorID(2L);;

        assertEquals("Failure - Number of pages found by findByAuthorID(2L) should be 1", 1, pages.size());
        assertEquals("Failure - Page found by findByAuthorID(2L) is not correct", testWikiPage2.getId(), pages.get(0).getId());

        List<Long> pageIDs = new ArrayList<>();
        pageIDs.add(testWikiPage3.getId());
        pageIDs.add(testWikiPage4.getId());

        pages = repo.findByAuthorID(3L);;

        assertEquals("Failure - Number of pages found by findByAuthorID(3L) should be 2", 2, pages.size());
        assertTrue("Failure - First page found by findByAuthorID(3L) is not correct", pageIDs.contains(pages.get(0).getId()));
        assertTrue("Failure - Second page found by findByAuthorID(3L) is not correct", pageIDs.contains(pages.get(1).getId()));

    }


    @Before
    public void setUp() throws Exception {
        testWikiPage1 = new WikiPage("testTitle1", "testContent1",1L);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(),2L);
        testWikiPage3 = new WikiPage("testTitlePair", "testContent1",3L);
        testWikiPage4 = new WikiPage("testTitlePair", "testContent2", testWikiPage3.getId(),3L);

        repo.save(testWikiPage1);
        repo.save(testWikiPage2);
        repo.save(testWikiPage3);
        repo.save(testWikiPage4);

    }

    @After
    public void tearDown() throws Exception {
        repo.deleteAll();
    }
}