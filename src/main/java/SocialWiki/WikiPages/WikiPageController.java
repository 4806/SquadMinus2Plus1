package SocialWiki.WikiPages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<WikiPage> createWikiPage(HttpServletRequest request) {

        //Retrieve parameters from request
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Long parentID, authorID;

        try {
            parentID = Long.parseLong(request.getParameter("parentID"));
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        try {
            authorID = Long.parseLong(request.getParameter("authorID"));
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        //Validate parameters

        if (title == null || title.isEmpty()) {    //title must be valid, non-empty string
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (content == null) {     //content must be valid string
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (parentID.compareTo(0L) == 0 || parentID.compareTo(-1L) < 0) {  //Parent ID must be > 0 or -1
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }
        else if (authorID.compareTo(0L) <= 0) {   //Author ID must be > 0
            return new ResponseEntity<>(new WikiPage(), HttpStatus.PRECONDITION_FAILED);
        }

        WikiPage newPage;

        //If the WikiPage being created has no predecessor and is original then use specific constructor
        if (parentID.compareTo(WikiPage.IS_ORIGINAL_ID) == 0) {
            newPage = new WikiPage(title, content, authorID);
        }
        else {
            newPage = new WikiPage(title, content, parentID, authorID);
        }

        //Save the WikiPage
        try {
            newPage = wikiPageRepo.save(newPage);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(newPage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(newPage, HttpStatus.OK);
    }


}
