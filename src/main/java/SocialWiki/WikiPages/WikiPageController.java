package SocialWiki.WikiPages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chris on 2/24/2017.
 */
@RestController
public class WikiPageController {

    /**
     * Repository for all WikiPages.
     */
    @Autowired
    private WikiPageRepository wikiPageRepo;

    /**
     * Method to handle the creation or editing of a WikiPage
     * @param request - contains the title, content, parentID, and authorID of the WikiPage being created/altered
     * @return the new WikiPage
     */
    @PostMapping("/createWikiPage")
    public WikiPage createWikiPage(HttpServletRequest request) {

        //Retrieve parameters from request
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Long parentID, authorID;

        try {
            parentID = Long.parseLong(request.getParameter("parentID"));
        } catch (NumberFormatException e) {
            return null;
        }

        try {
            authorID = Long.parseLong(request.getParameter("authorID"));
        } catch (NumberFormatException e) {
            return null;
        }

        WikiPage newPage;

        //If the WikiPage being created has no predecessor and is original then use specific constructor
        if (parentID.compareTo(WikiPage.IS_ORIGINAL_ID) == 0) {
            newPage = new WikiPage(title,content,authorID);
        }
        else {
            newPage = new WikiPage(title,content,parentID,authorID);
        }

        return wikiPageRepo.save(newPage);
    }


}
