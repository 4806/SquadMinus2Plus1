package WikiPages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chris on 2/24/2017.
 */
public class WikiPageTest {

    WikiPage testWikiPage1;
    WikiPage testWikiPage2;

    @Test
    public void getTitle() throws Exception {
        assertEquals("Failure - getTitle for testWikiPage1", testWikiPage1.getTitle(), "testTitle1");
        assertEquals("Failure - getTitle for testWikiPage2", testWikiPage2.getTitle(), "testTitle2");
    }

    @Test
    public void getContent() throws Exception {
        assertEquals("Failure - getContent for testWikiPage1", testWikiPage1.getContent(), "testContent1");
        assertEquals("Failure - getContent for testWikiPage2", testWikiPage2.getContent(), "testContent2");
    }

    @Test
    public void getParentID() throws Exception {
        assertEquals("Failure - getParentID for testWikiPage1", testWikiPage1.getParentID(), WikiPage.isOriginalID);
        assertEquals("Failure - getParentID for testWikiPage2", testWikiPage2.getParentID(), testWikiPage1.getId());
    }

    @Test
    public void getAuthorID() throws Exception {
        assertEquals("Failure - getAuthorID for testWikiPage1", testWikiPage1.getAuthorID(), new Long(1L));
        assertEquals("Failure - getAuthorID for testWikiPage2", testWikiPage2.getAuthorID(), new Long(2L));
    }

    @Before
    public void setUp() throws Exception {
        testWikiPage1 = new WikiPage("testTitle1", "testContent1",1L);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(),2L);

    }
}