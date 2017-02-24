package WikiPages;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by Chris on 2/24/2017.
 */
public class WikiPageTest {

    WikiPage testWikiPage1;
    WikiPage testWikiPage2;

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
    public void getAuthorID() throws Exception {
        assertEquals("Failure - getAuthorID for testWikiPage1", new Long(1L), testWikiPage1.getAuthorID());
        assertEquals("Failure - getAuthorID for testWikiPage2", new Long(2L), testWikiPage2.getAuthorID());
    }

    @Test
    public void getCreationDate() throws Exception {

        Calendar date1 = testWikiPage1.getCreationDate();
        Calendar date2 = testWikiPage2.getCreationDate();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        assertEquals("Failure - getCreationDate for testWikiPage1", dateFormat.format(now.getTime()), dateFormat.format(date1.getTime()));
        assertEquals("Failure - getCreationDate for testWikiPage2", dateFormat.format(now.getTime()), dateFormat.format(date2.getTime()));
    }

    @Before
    public void setUp() throws Exception {
        testWikiPage1 = new WikiPage("testTitle1", "testContent1",1L);
        testWikiPage2 = new WikiPage("testTitle2", "testContent2", testWikiPage1.getId(),2L);

    }
}